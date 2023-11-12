package top.sharehome.mailmodule.service;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.sharehome.mailmodule.utils.EmailUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;

/**
 * 邮箱发送简单文本服务类
 *
 * @author AntonyCheng
 * @since 2023/7/8 22:42:02
 */
@Component
@Slf4j
public class MailSimpleService {
    /**
     * 注入发送邮件的接口
     */
    @Resource
    private EmailUtil emailUtil;

    public static final String MAIL_MODULE_SIMPLE_QUEUE = "mail_module_simple_queue";

    @RabbitHandler
    @RabbitListener(queues = MailSimpleService.MAIL_MODULE_SIMPLE_QUEUE)
    public void mailMq1(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitResult = JSONObject.parseObject(new String(message.getBody()));
            String to = (String) rabbitResult.get("to");
            String subject = (String) rabbitResult.get("subject");
            String content = (String) rabbitResult.get("content");
            emailUtil.sendSimpleMail(to, subject, content);
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

    @RabbitHandler
    @RabbitListener(queues = MailSimpleService.MAIL_MODULE_SIMPLE_QUEUE)
    public void mailMq2(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitResult = JSONObject.parseObject(new String(message.getBody()));
            String to = (String) rabbitResult.get("to");
            String subject = (String) rabbitResult.get("subject");
            String content = (String) rabbitResult.get("content");
            emailUtil.sendSimpleMail(to, subject, content);
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

    @RabbitHandler
    @RabbitListener(queues = MailSimpleService.MAIL_MODULE_SIMPLE_QUEUE)
    public void mailMq3(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitResult = JSONObject.parseObject(new String(message.getBody()));
            String to = (String) rabbitResult.get("to");
            String subject = (String) rabbitResult.get("subject");
            String content = (String) rabbitResult.get("content");
            emailUtil.sendSimpleMail(to, subject, content);
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

    @RabbitHandler
    @RabbitListener(queues = MailSimpleService.MAIL_MODULE_SIMPLE_QUEUE)
    public void mailMq4(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitResult = JSONObject.parseObject(new String(message.getBody()));
            String to = (String) rabbitResult.get("to");
            String subject = (String) rabbitResult.get("subject");
            String content = (String) rabbitResult.get("content");
            emailUtil.sendSimpleMail(to, subject, content);
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

    @RabbitHandler
    @RabbitListener(queues = MailSimpleService.MAIL_MODULE_SIMPLE_QUEUE)
    public void mailMq5(Message message, Channel channel) {
        long deliveryTag = 0;
        try {
            HashMap<String, Object> rabbitResult = JSONObject.parseObject(new String(message.getBody()));
            String to = (String) rabbitResult.get("to");
            String subject = (String) rabbitResult.get("subject");
            String content = (String) rabbitResult.get("content");
            emailUtil.sendSimpleMail(to, subject, content);
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
