package top.sharehome.usermodule.lister.manager;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.sharehome.usermodule.mapper.ManagerMapper;
import top.sharehome.usermodule.model.entity.Manager;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * 管理者消息队列监听器
 *
 * @author AntonyCheng
 * @since 2023/7/23 00:17:58
 */
@Component
@Slf4j
public class ManagerUpdateLister {

    @Resource
    private ManagerMapper managerMapper;

    @RabbitHandler
    @RabbitListener(queues = "${tid}_manager_update_queue")
    public void userMq(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitMqManagerResult = JSONObject.parseObject(new String(message.getBody()));
            Long tid = (Long) rabbitMqManagerResult.get("tid");
            String password = (String) rabbitMqManagerResult.get("password");
            if (Objects.nonNull(password)) {
                Manager manager = new Manager();
                manager.setPassword(password);
                LambdaUpdateWrapper<Manager> managerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                managerLambdaUpdateWrapper.eq(Manager::getTenant, tid);
                managerMapper.update(manager, managerLambdaUpdateWrapper);
            } else {
                String account = (String) rabbitMqManagerResult.get("account");
                String name = (String) rabbitMqManagerResult.get("name");
                Integer gender = (Integer) rabbitMqManagerResult.get("gender");
                String email = (String) rabbitMqManagerResult.get("email");
                String picture = (String) rabbitMqManagerResult.get("picture");
                Integer level = (Integer) rabbitMqManagerResult.get("level");
                Manager manager = new Manager();
                manager.setAccount(account);
                manager.setName(name);
                manager.setGender(gender);
                manager.setEmail(email);
                manager.setPicture(picture);
                manager.setLevel(level);
                LambdaUpdateWrapper<Manager> managerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                managerLambdaUpdateWrapper.eq(Manager::getTenant, tid);
                managerMapper.update(manager, managerLambdaUpdateWrapper);
            }
            deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
