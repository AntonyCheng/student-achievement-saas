package top.sharehome.redisclusterdemo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.time.Duration;

@SpringBootTest
class RedisClusterDemoApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisCluster() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        for (int i = 0; i < 2; i++) {
            valueOperations.set(String.valueOf(i), i, Duration.ofSeconds(300));
//            redisTemplate.delete(i);
        }
    }

}
