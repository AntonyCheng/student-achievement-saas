package top.sharehome.gatewayrouterdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class GatewayRouterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayRouterDemoApplication.class, args);
    }

}
