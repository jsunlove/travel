package com.qingmu.travel.pojo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class Travel {
    /**
     * 出差id号
     */
    private Integer id;
    /**
     * 出差地点
     */
    private String space;
    /**
     * 出差项目类型
     */
    private String item;
    /**
     * 出差日期
     */
    private String start_date;
    /**
     * 出差结束日期
     */
    private String stop_date;
    /**
     * 出差天数
     */
    private Integer travel_days;
    /**
     * 出差人员
     */
    private String travel_person;
    /**
     * 完成工作
     */
    private String finish_work;
    /**
     * 问题总结
     */
    private String problem_sum;

    /**
     * 附件名字
     */
    private String file_name;
    /**
     * 附件路径
     */
    private String file_path;
    /**
     * 附件UUID名
     */
    private String file_uuid;
}
