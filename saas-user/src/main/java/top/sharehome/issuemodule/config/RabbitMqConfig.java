package top.sharehome.issuemodule.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * RabbitMQ 配置类
 *
 * @author AntonyCheng
 */
@Configuration
public class RabbitMqConfig {

    public static String MANAGER_UPDATE_EXCHANGE;
    public static String MANAGER_UPDATE_QUEUE;

    @Value("${tid}")
    private String tid;

    @PostConstruct
    private void init() {
        MANAGER_UPDATE_EXCHANGE = tid + "_manager_update_exchange";
        MANAGER_UPDATE_QUEUE = tid + "_manager_update_queue";
    }

    @Resource
    private RabbitProperties rabbitProperties;

    @Bean
    public ConnectionFactory clusterConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitProperties.getAddresses());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        connectionFactory.setPublisherConfirmType(rabbitProperties.getPublisherConfirmType());
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate noSingletonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //设置消息进入交换机后未被队列接收的消息不被丢弃由broker保存,false为丢弃
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReceiveTimeout(30000);
        rabbitTemplate.setReplyTimeout(30000);
        return rabbitTemplate;
    }

    @Bean
    public Exchange userExchange() {
        return new TopicExchange(MANAGER_UPDATE_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue userQueue(ConnectionFactory connectionFactory) {
        Queue queue = new Queue(MANAGER_UPDATE_QUEUE, true, false, false, null);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding userBinding(Queue userQueue, Exchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(tid + "_manager_update.*").noargs();
    }
}
