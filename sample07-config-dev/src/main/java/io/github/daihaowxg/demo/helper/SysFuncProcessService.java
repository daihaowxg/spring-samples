package io.github.daihaowxg.demo.helper;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.daihaowxg.demo.domain.SysFuncProcess;

public interface SysFuncProcessService extends IService<SysFuncProcess> {

    SysFuncProcess getByFuncId(String funcId);
}
