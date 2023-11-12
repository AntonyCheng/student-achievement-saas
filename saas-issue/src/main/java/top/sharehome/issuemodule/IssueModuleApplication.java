package top.sharehome.issuemodule;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * IssueModule启动类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@ComponentScan(basePackages = "top.sharehome")
@Slf4j
@MapperScan("top.sharehome.issuemodule.mapper")
@EnableDubbo
public class IssueModuleApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(IssueModuleApplication.class, args);
    }

}
