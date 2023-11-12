package top.sharehome.usergatewaymodule;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserGatewayModule1ApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        String url = "/ug/login";
        String substring = url.substring(4);
        System.out.println(substring);
    }

}
