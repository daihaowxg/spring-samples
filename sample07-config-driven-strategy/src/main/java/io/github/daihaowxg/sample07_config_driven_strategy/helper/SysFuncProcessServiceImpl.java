package io.github.daihaowxg.sample07_config_driven_strategy.helper;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.daihaowxg.sample07_config_driven_strategy.domain.SysFuncProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysFuncProcessServiceImpl extends ServiceImpl<SysFuncProcessMapper, SysFuncProcess> implements SysFuncProcessService {

    @Override
    public SysFuncProcess getByFuncId(String funcId) {
        List<SysFuncProcess> processList = lambdaQuery()
            .eq(SysFuncProcess::getFuncId, funcId)
            .list();
        if (processList.isEmpty()) {
            return null;
        }
        if (processList.size() > 1) {
            log.warn("funcId={} 存在多条策略配置，将使用第一条记录", funcId);
        }

        return processList.getFirst();
    }
}
