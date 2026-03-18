package io.github.daihaowxg.sample07_config_driven_strategy.helper;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.daihaowxg.sample07_config_driven_strategy.domain.SysFuncProcess;

public interface SysFuncProcessService extends IService<SysFuncProcess> {

    SysFuncProcess getByFuncId(String funcId);
}
