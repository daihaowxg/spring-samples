package io.github.daihaowxg.demo.helper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.daihaowxg.demo.domain.SysFuncProcess;
import org.springframework.stereotype.Service;

@Service
public class SysFuncProcessServiceImpl extends ServiceImpl<SysFuncProcessMapper, SysFuncProcess> implements SysFuncProcessService {

    public SysFuncProcess getByFuncId(String funcId) {
        QueryWrapper<SysFuncProcess> query = new QueryWrapper<>();
        query.eq("func_id", funcId);

        return super.getOne(query);
    }
}
