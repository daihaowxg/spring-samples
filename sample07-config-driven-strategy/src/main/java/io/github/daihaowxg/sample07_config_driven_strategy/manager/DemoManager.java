package io.github.daihaowxg.sample07_config_driven_strategy.manager;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.github.daihaowxg.sample07_config_driven_strategy.domain.SysFuncProcess;
import io.github.daihaowxg.sample07_config_driven_strategy.helper.SysFuncProcessService;
import io.github.daihaowxg.sample07_config_driven_strategy.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 可以根据 funcId 动态切换实现
 */
@Component
@RequiredArgsConstructor
public class DemoManager {

    private SysFuncProcessService sysFuncProcessService;


    /**
     * 可以根据 funcId 动态切换实现
     *
     * @param funcId 功能编号
     * @return something
     */
    public Object doSomething(String funcId) {
        // 根据功能编号获取个性化实现
        SysFuncProcess sysFuncProcess = sysFuncProcessService.getByFuncId(funcId);
        if (sysFuncProcess != null) {
            String className = sysFuncProcess.getClassName();
            if (StrUtil.isNotBlank(className)) {
                DemoService demoService = SpringUtil.getBean(className);
                if (demoService != null) {
                    return demoService.doSomething();
                }
            }
        }

        // 回退到默认的实现
        DemoService demoService = SpringUtil.getBean("demoBaseService");
        return demoService.doSomething();
    }

}
