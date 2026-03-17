package io.github.daihaowxg.demo.controller;

import io.github.daihaowxg.demo.manager.DemoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private DemoManager demoManager;


    @GetMapping("/demo")
    public Object demo(@RequestParam String funcId) {
        // 调用 Manager，根据 funcId 动态切换实现类
        return demoManager.doSomething(funcId);
    }
}
