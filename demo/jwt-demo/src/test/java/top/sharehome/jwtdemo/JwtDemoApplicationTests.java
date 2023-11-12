package top.sharehome.jwtdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.sharehome.jwtdemo.entity.vo.LoginVo;
import top.sharehome.jwtdemo.utils.JwtUtils;

import javax.annotation.Resource;

@SpringBootTest
class JwtDemoApplicationTests {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void testCreate() {
        String token = jwtUtils.createLoginToken(new LoginVo());
        System.out.println(token);
    }

    @Test
    void testCheck() {
        redisTemplate.opsForValue().set("token_" + 1, 1);
        Object o = redisTemplate.opsForValue().get("token_" + 1);
        System.out.println("o = " + o);
    }

    public static void main(String[] args) {

    }
}
