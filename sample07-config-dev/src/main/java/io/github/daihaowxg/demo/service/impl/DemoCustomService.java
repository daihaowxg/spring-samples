package io.github.daihaowxg.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 个性化实现业务逻辑
 */
@Service
@Slf4j
public class DemoCustomService extends DemoBaseService {


    @Override
    public Object doSomething() {
        log.info("执行个性化的逻辑，这里可以直接重写基础实现的全部逻辑");
        return super.doSomething();
    }


    @Override
    protected void afterDoSomething() {
        log.info("子类可以重写个性化的后置逻辑");
    }

    @Override
    protected Object doSomethingInternal() {
        log.info("子类可以重写个性化的核心逻辑");
        return super.doSomethingInternal();
    }

    @Override
    protected void beforeDoSomething() {
        log.info("子类可以重写个性化的前置逻辑");
        super.beforeDoSomething();
    }
}
