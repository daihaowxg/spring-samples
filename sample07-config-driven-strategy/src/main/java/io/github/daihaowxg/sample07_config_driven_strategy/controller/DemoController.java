package io.github.daihaowxg.sample07_config_driven_strategy.controller;

import io.github.daihaowxg.sample07_config_driven_strategy.dto.DemoResult;
import io.github.daihaowxg.sample07_config_driven_strategy.manager.DemoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoManager demoManager;


    @GetMapping("/demo")
    public DemoResult demo(@RequestParam String funcId) {
        // 调用 Manager，根据 funcId 动态切换实现类
        return demoManager.doSomething(funcId);
    }
}
