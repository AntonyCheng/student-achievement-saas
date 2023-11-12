package top.sharehome.issuemodule;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import top.sharehome.issuemodule.mapper.TenantMapper;
import top.sharehome.issuemodule.mapper.TokenMapper;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.entity.Update;
import top.sharehome.issuemodule.service.UpdateService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class IssueModuleApplicationTests {
    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private UpdateService updateService;

    @Test
    void testWrite() {
        Token tokenUser = new Token(null, "17380271985", "123456", 1L, "3", 1, null, null, null);
        int insert = tokenMapper.insert(tokenUser);
        log.info("Insert result: " + insert);
    }

    @Test
    void testRead() {
//        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        tokenLambdaUpdateWrapper.eq(Token::getAccount,"zzzz");
//        List<Token> tokens = tokenMapper.selectList(tokenLambdaUpdateWrapper);
//        System.out.println(tokens);
        List<Update> list = updateService.list();
        System.out.println(list);
    }

    @Test
    void testUpdate() {
        LambdaUpdateWrapper<Tenant> tenantLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tenantLambdaUpdateWrapper.eq(Tenant::getId, 1678435349939802114L);
        tenantMapper.update(new Tenant() {
            {
                setIp("127.0.0.1");
                setPort("40002");
            }
        }, tenantLambdaUpdateWrapper);
    }

    @Test
    void testDelete() {
        LambdaUpdateWrapper<Token> tokenLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        tokenLambdaUpdateWrapper.eq(Token::getAccount, "user");
        tokenMapper.delete(tokenLambdaUpdateWrapper);
    }

    @Test
    void testAES() {
        String s = DigestUtils.md5DigestAsHex("admin".getBytes(StandardCharsets.UTF_8));
        System.out.println(s);
    }

    @Test
    public void testPublishConfig() {
        try {
            String serverAddr = "xxx.xxx.xxx.xxx:28848";
            String dataId = "userGateway";
            String group = "DEFAULT_GROUP";
            String username = "nacos";
            String password = "admin123456";
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("username", username);
            properties.put("password", password);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            boolean b = configService.publishConfig("top.sharehome.gateway.user", "DEFAULT_GROUP", content, "YAML");
            System.out.println(b);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        System.out.println("TimeUnit.MINUTES.toSeconds(9) = " + TimeUnit.MINUTES.toSeconds(9));
    }
}
