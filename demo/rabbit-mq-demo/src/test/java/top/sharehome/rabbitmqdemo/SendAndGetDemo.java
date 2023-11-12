package top.sharehome.rabbitmqdemo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.rabbitmqdemo.config.RabbitMqConfig;

import javax.annotation.Resource;

/**
 * 消息发送者
 *
 * @author AntonyCheng
 * @since 2023/7/1 10:28:29
 */
@SpringBootTest
public class SendAndGetDemo {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "demo.demo", "demo");
    }
}