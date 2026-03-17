package io.github.daihaowxg.demo.domain;


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
    @TableId
    private String id;

    /**
     * 功能编号
     */
    @TableField
    private String funcId;

    /**
     * 实现类
     */
    @TableField
    private String className;
}
