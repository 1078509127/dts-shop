package com.qiguliuxing.dts.wx.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class DtsReserveVo implements Serializable {

    private Integer id;
    /**
     *用户id
     * */
    private Integer userId;
    /**
     *个人姓名/团队负责人姓名
     * */
    private String userName;
    /**
     *个人手机号/团队负责人手机号
     * */
    private String phone;
    /**
     *性别
     * */
    private String sex;
    /**
     *工作单位
     * */
    private String unit;
    /**
     *预约开始时间年月日
     * */
    private String date;
    /**
     *预约开始时间时分秒
     * */
    private String startTime;
    /**
     *预约结束时间时分秒
     * */
    private String endTime;
    /**
     *会员卡号（个人）
     * */
    private String memberCard;
    /**
     *活动人数（团队）
     * */
    private String activeNumber;
    /**
     *活动内容描述（团队）
     * */
    private String remark;
    /**
     *桌号（仅限乒乓球室）
     * */
    private String tableNumber;
    /**
     *预约类型（个人预约/团队预约）
     * */
    private String eventType;
    /**
     *场景（乒乓球馆/瑜伽师等）
     * */
    private String scene;
}
