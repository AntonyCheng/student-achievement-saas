package top.sharehome.consumerdemo.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sharehome.providerdemo.service.DemoService;

/**
 * Demo控制器
 *
 * @author AntonyCheng
 * @since 2023/7/2 10:02:33
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @DubboReference
    private DemoService demoService;

    @GetMapping("/name")
    public String name() {
        return demoService.sayName("name");
    }

    @GetMapping("/hello")
    public String hello() {
        return demoService.sayHello("hello");
    }
}
