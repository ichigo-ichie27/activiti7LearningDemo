package com.lc.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 出差申请中流程变量对象
 */
@Getter
@Setter
public class Evection implements Serializable{

    /**
     * 主键id
     */
    private Long id;

    /**
     * 出差天数
     */
    private Double days;

    /**
     * 出差单的名称
     */
    private String evectionName;

    /**
     * 出差开始时间
     */
    private Date startDate;

    /**
     * 出差结束时间
     */
    private Date endDate;

    /**
     * 出差原因
     */
    private String reson;

    /**
     * 出差目的地
     */
    private String destination;
}
