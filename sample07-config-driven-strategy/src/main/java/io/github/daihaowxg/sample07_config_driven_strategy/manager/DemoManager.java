package io.github.daihaowxg.sample07_config_driven_strategy.manager;


import java.util.Map;

import io.github.daihaowxg.sample07_config_driven_strategy.dto.DemoResult;
import io.github.daihaowxg.sample07_config_driven_strategy.domain.SysFuncProcess;
import io.github.daihaowxg.sample07_config_driven_strategy.helper.SysFuncProcessService;
import io.github.daihaowxg.sample07_config_driven_strategy.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 可以根据 funcId 动态切换实现
 */
@Component
@Slf4j
public class DemoManager {

    private static final String DEFAULT_BEAN_NAME = "demoBaseService";

    private final SysFuncProcessService sysFuncProcessService;

    private final Map<String, DemoService> demoServiceRegistry;


    public DemoManager(SysFuncProcessService sysFuncProcessService, Map<String, DemoService> demoServiceRegistry) {
        this.sysFuncProcessService = sysFuncProcessService;
        this.demoServiceRegistry = demoServiceRegistry;
    }


    /**
     * 可以根据 funcId 动态切换实现
     *
     * @param funcId 功能编号
     * @return 执行结果
     */
    public DemoResult doSomething(String funcId) {
        DemoService demoService = getDefaultDemoService();
        String configuredBeanName = null;
        String appliedBeanName = DEFAULT_BEAN_NAME;
        boolean fallback = true;

        SysFuncProcess sysFuncProcess = sysFuncProcessService.getByFuncId(funcId);
        if (sysFuncProcess != null && StringUtils.hasText(sysFuncProcess.getBeanName())) {
            configuredBeanName = sysFuncProcess.getBeanName();
            DemoService configuredDemoService = demoServiceRegistry.get(configuredBeanName);
            if (configuredDemoService != null) {
                demoService = configuredDemoService;
                appliedBeanName = configuredBeanName;
                fallback = false;
            } else {
                log.warn("funcId={} 配置了不存在的 Bean: {}，将回退到默认实现", funcId, configuredBeanName);
            }
        } else {
            log.info("funcId={} 未命中个性化配置，使用默认实现", funcId);
        }

        return new DemoResult(funcId, configuredBeanName, appliedBeanName, fallback, demoService.doSomething());
    }


    private DemoService getDefaultDemoService() {
        DemoService demoService = demoServiceRegistry.get(DEFAULT_BEAN_NAME);
        if (demoService == null) {
            throw new IllegalStateException("未找到默认策略 Bean: " + DEFAULT_BEAN_NAME);
        }
        return demoService;
    }
}
