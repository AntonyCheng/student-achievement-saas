package top.sharehome.providerdemo.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import top.sharehome.providerdemo.service.DemoService;

/**
 * @author AntonyCheng
 * @since 2023/7/1 23:58:10
 */
@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayName(String name) {
        return name;
    }

    @Override
    public String sayHello(String hello) {
        return hello;
    }
}
