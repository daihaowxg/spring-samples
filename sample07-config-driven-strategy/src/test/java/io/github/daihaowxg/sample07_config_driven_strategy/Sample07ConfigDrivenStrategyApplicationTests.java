package io.github.daihaowxg.sample07_config_driven_strategy;

import io.github.daihaowxg.sample07_config_driven_strategy.dto.DemoResult;
import io.github.daihaowxg.sample07_config_driven_strategy.manager.DemoManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Sample07ConfigDrivenStrategyApplicationTests {

    @Autowired
    private DemoManager demoManager;


    @Test
    void contextLoads() {
    }


    @Test
    void doSomething_customStrategy() {
        DemoResult result = demoManager.doSomething("custom-func");
        assertEquals("demoCustomService", result.appliedBeanName());
        assertEquals("demoCustomService", result.configuredBeanName());
        assertEquals("custom-success", result.result());
        assertFalse(result.fallback());
    }


    @Test
    void doSomething_defaultStrategy() {
        DemoResult result = demoManager.doSomething("unknown-func");
        assertEquals("demoBaseService", result.appliedBeanName());
        assertNull(result.configuredBeanName());
        assertEquals("base-success", result.result());
        assertTrue(result.fallback());
    }


    @Test
    void doSomething_missingBeanFallback() {
        DemoResult result = demoManager.doSomething("missing-bean-func");
        assertEquals("missingDemoService", result.configuredBeanName());
        assertEquals("demoBaseService", result.appliedBeanName());
        assertEquals("base-success", result.result());
        assertTrue(result.fallback());
    }
}
