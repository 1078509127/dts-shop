package com.qiguliuxing.dts.wx.dao;

import java.io.Serializable;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;
@Data
public class DtsRreserveVOVO implements Serializable {
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *用户id
     * */
    @ExcelProperty("id")
    private Integer userId;
    /**
     *个人姓名/团队负责人姓名
     * */
    @ExcelProperty("姓名")
    private String userName;
    /**
     *个人手机号/团队负责人手机号
     * */
    @ColumnWidth(20)
    @ExcelProperty("手机号")
    private String phone;
    /**
     *性别
     * */
    @ExcelProperty("性别")
    private String sex;
    /**
     *工作单位
     * */
    @ColumnWidth(20)
    @ExcelProperty("工作单位")
    private String unit;
    /**
     *预约开始时间  string
     * */


    @ColumnWidth(25)
    @ExcelProperty("开始时间")
    private Date startTime;
    /**
     *预约结束时间
     * */
    @ColumnWidth(25)
    @ExcelProperty("结束时间")
    private Date endTime;
    /**
     *会员卡号（个人）
     * */
    @ColumnWidth(15)
    @ExcelProperty("会员卡号")
    private String memberCard;
    /**
     *活动人数（团队）
     * */
    @ColumnWidth(15)
    @ExcelProperty("活动人数")
    private String activeNumber;
    /**
     *活动内容描述（团队）
     * */
    @ColumnWidth(30)
    @ExcelProperty("活动内容描述")
    private String remark;
    /**
     *桌号（仅限乒乓球室）
     * */
    @ColumnWidth(20)
    @ExcelProperty("乒乓球桌号")
    private String tableNumber;
    /**
     *预约类型（个人预约/团队预约）
     * */
    @ColumnWidth(30)
    @ExcelProperty("个人预约/团队预约")
    private String eventType;
    /**
     *场景（乒乓球馆/瑜伽师等）
     * */
    @ColumnWidth(20)
    @ExcelProperty("场景")
    private String scene;

//    @ExcelProperty("创建日期")//时间
//    private Date createTime;


    /**
     *是否取消（0预约/1取消）
     * */
    @Value("0")
    @ExcelIgnore
    private Integer isReserve;

    /**
     * 健身房/图书管使用次数
     * */
    @ColumnWidth(15)
    @ExcelProperty("进场次数")
    private Integer times;

//    @ExcelProperty("时间")
//    private Date time;
//
//
//
//
//    /**
//     //     *预约开始时间年月日
//     //     * */
//    private String date;
//
//
//    // 预约驳回
//    private String disallowance;

    //日期
    @ColumnWidth(25)
    @ExcelProperty("创建日期")
    private  String reserveDate;
    // 时间excel  显示时分秒
    @ColumnWidth(25)
    @ExcelProperty("创建时间")
    private String reservetime;

}
