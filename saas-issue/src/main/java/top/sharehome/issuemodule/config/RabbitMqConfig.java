package top.sharehome.issuemodule.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * RabbitMQ 配置类
 *
 * @author AntonyCheng
 */
@Configuration
public class RabbitMqConfig {
    public static final String MAIL_MODULE_HTML_EXCHANGE = "mail_module_html_exchange";

    public static final String MAIL_MODULE_HTML_QUEUE = "mail_module_html_queue";

    public static final String MAIL_MODULE_SIMPLE_EXCHANGE = "mail_module_simple_exchange";

    public static final String MAIL_MODULE_SIMPLE_QUEUE = "mail_module_simple_queue";

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
    public Exchange mailModuleHtmlExchange() {
        return new TopicExchange(MAIL_MODULE_HTML_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue mailModuleHtmlQueue(ConnectionFactory connectionFactory) {
        Queue queue = new Queue(MAIL_MODULE_HTML_QUEUE, true, false, false, null);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding mailModuleHtmlBinding(Queue mailModuleHtmlQueue, Exchange mailModuleHtmlExchange) {
        return BindingBuilder.bind(mailModuleHtmlQueue).to(mailModuleHtmlExchange).with("mail_module_html.*").noargs();
    }

    @Bean
    public Exchange mailModuleSimpleExchange(){
        return new TopicExchange(MAIL_MODULE_SIMPLE_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue mailModuleSimpleQueue(ConnectionFactory connectionFactory){
        Queue queue = new Queue(MAIL_MODULE_SIMPLE_QUEUE, true, false, false, null);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding mailModuleSimpleBinding(Queue mailModuleSimpleQueue, Exchange mailModuleSimpleExchange){
        return BindingBuilder.bind(mailModuleSimpleQueue).to(mailModuleSimpleExchange).with("mail_module_simple.*").noargs();
    }
}
