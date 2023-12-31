package top.sharehome.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@SpringBootApplication
public class RabbitMqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqDemoApplication.class, args);
    }

    @RabbitHandler
    @RabbitListener(queues = "DemoQueue")
    public void messageConsumer(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            System.out.println(new String(message.getBody()));
            // 模拟出现异常
            // System.out.println(1 / 0);
            // 手动确认
            // 参数1：消息的tag
            // 参数2：多条处理
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 如果出现异常的情况下 根据实际情况重发
            // 重发一次后，丢失
            // 参数1：消息的tag
            // 参数2：多条处理
            // 参数3：重发
            //      false 不会重发，会把消息打入到死信队列
            //      true 重发，建议不使用try/catch 否则会死循环
            // 手动拒绝消息
            channel.basicNack(tag, false, true);
        }
    }
}
