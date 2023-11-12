package top.sharehome.mailmodule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import org.apache.http.util.CharsetUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.mailmodule.utils.EmailUtil;

import javax.annotation.Resource;
import java.io.BufferedReader;

@SpringBootTest
class MailModuleApplicationTests {
    /**
     * 注入发送邮件的接口
     */
    @Resource
    private EmailUtil emailUtil;

    @Test
    void contextLoads() {
        emailUtil.sendHtmlMail("xxx@qq.com", "xxx科技有限公司", "<body>\n" +
                "<h1>xxx科技有限公司</h1>\n" +
                "<p style=\"font-size: x-large\">很抱歉！您的注册申请已经审核驳回，注册账号为“<b>17399999999</b>”，详情请查看备注内容。</p>\n" +
                "<p style=\"font-size: x-large\"></p>\n" +
                "<p style=\"font-size: large\"><b>备注：</b>该账号所填写情况不属实，不予以审核通过，如有需要，请改正后重新提交注册申请！</p>\n" +
                "</body>");
    }
}
