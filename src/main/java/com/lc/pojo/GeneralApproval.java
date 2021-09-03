package com.lc.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GeneralApproval implements Serializable{

    /**
     * 操作类型
     * 0 调整
     * 1 提交
     */
    private Integer operateType;

    /**
     * 判断类型
     * 0通过
     * 1不通过
     * 2允许
     * 3不允许
     */
    private Integer judgeType;
}
