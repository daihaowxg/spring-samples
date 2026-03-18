package io.github.daihaowxg.sample07_config_driven_strategy.service.impl;

import io.github.daihaowxg.sample07_config_driven_strategy.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 基础实现
 */
@Service
@Slf4j
public class DemoBaseService implements DemoService {
    @Override
    public String doSomething() {
        log.info("这里是默认的实现方法");
        beforeDoSomething();
        String result = doSomethingInternal();
        afterDoSomething();
        return result;
    }

    protected void afterDoSomething() {
        log.info("执行后置逻辑");

    }

    protected String doSomethingInternal() {
        log.info("执行核心逻辑");
        return "base-success";
    }

    protected void beforeDoSomething() {
        log.info("执行前置逻辑");
    }
}
