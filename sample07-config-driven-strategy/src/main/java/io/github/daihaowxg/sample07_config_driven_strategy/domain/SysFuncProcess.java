package io.github.daihaowxg.sample07_config_driven_strategy.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_func_process")
public class SysFuncProcess {
    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 功能编号
     */
    @TableField("func_id")
    private String funcId;

    /**
     * Spring Bean 名称
     */
    @TableField("bean_name")
    private String beanName;
}
