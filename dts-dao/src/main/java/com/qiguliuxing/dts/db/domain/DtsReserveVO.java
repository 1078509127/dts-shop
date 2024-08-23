package com.qiguliuxing.dts.db.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Data
public class DtsReserveVO implements Serializable {

//    @TableId(value = "id", type = IdType.AUTO)
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
     *预约开始时间
     * */
    private Date startTime;



    /**
     *预约结束时间
     * */
    private Date endTime;
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

    private Date createTime;

    private Date time;


    /**
     *是否取消（0预约/1取消）
     * */
    @Value("0")
    private Integer isReserve;

    /**
     * 健身房/图书管使用次数
     * */

    private Integer times;

    // 预约驳回
    private String disallowance;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
    }

    public String getActiveNumber() {
        return activeNumber;
    }

    public void setActiveNumber(String activeNumber) {
        this.activeNumber = activeNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getIsReserve() {
        return isReserve;
    }

    public void setIsReserve(Integer isReserve) {
        this.isReserve = isReserve;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getDisallowance() {
        return disallowance;
    }

    public void setDisallowance(String disallowance) {
        this.disallowance = disallowance;
    }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName());
            sb.append(" [");
            sb.append("Hash = ").append(hashCode());
            sb.append(", id=").append(id);
            sb.append(", userId=").append(userId);
            sb.append(", userName=").append(userName);
            sb.append(", phone=").append(phone);
            sb.append(", sex=").append(sex);
            sb.append(", unit=").append(unit);
            sb.append(", startTime=").append(startTime);
            sb.append(", endTime=").append(endTime);
            sb.append(", memberCard=").append(memberCard);
            sb.append(", activeNumber=").append(activeNumber);
            sb.append(", remark=").append(remark);
            sb.append(", tableNumber=").append(tableNumber);
            sb.append(", eventType=").append(eventType);
            sb.append(", scene=").append(scene);
            sb.append(", createTime=").append(createTime);
            sb.append(", isReserve=").append(isReserve);
            sb.append(", times=").append(times);
            sb.append(", time=").append(time);
            sb.append(", disallowance=").append(disallowance);

            sb.append("]");
            return sb.toString();
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table dts_region
         *
         * @mbg.generated
         */
        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (that == null) {
                return false;
            }
            if (getClass() != that.getClass()) {
                return false;
            }
            com.qiguliuxing.dts.db.domain.DtsReserveVO other = (com.qiguliuxing.dts.db.domain.DtsReserveVO) that;
            return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                    && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                    && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
                    && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                    && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
                    && (this.getUnit() == null ? other.getUnit() == null : this.getUnit().equals(other.getUnit()))
                    && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
                    && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
                    && (this.getMemberCard() == null ? other.getMemberCard() == null : this.getMemberCard().equals(other.getMemberCard()))
                    && (this.getActiveNumber() == null ? other.getActiveNumber() == null : this.getActiveNumber().equals(other.getActiveNumber()))
                    && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                    && (this.getTableNumber() == null ? other.getTableNumber() == null : this.getTableNumber().equals(other.getTableNumber()))
                    && (this.getEventType() == null ? other.getEventType() == null : this.getEventType().equals(other.getEventType()))
                    && (this.getScene() == null ? other.getScene() == null : this.getScene().equals(other.getScene()))
                    && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                    && (this.getIsReserve() == null ? other.getIsReserve() == null : this.getIsReserve().equals(other.getIsReserve()))
                    && (this.getTimes() == null ? other.getTimes() == null : this.getTimes().equals(other.getTimes()))
                    && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
                    && (this.getDisallowance() == null ? other.getDisallowance() == null : this.getDisallowance().equals(other.getDisallowance()));

        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table dts_region
         *
         * @mbg.generated
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
            result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
            result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
            result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
            result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
            result = prime * result + ((getUnit() == null) ? 0 : getUnit().hashCode());
            result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
            result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
            result = prime * result + ((getMemberCard() == null) ? 0 : getMemberCard().hashCode());
            result = prime * result + ((getActiveNumber() == null) ? 0 : getActiveNumber().hashCode());
            result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
            result = prime * result + ((getTableNumber() == null) ? 0 : getTableNumber().hashCode());
            result = prime * result + ((getEventType() == null) ? 0 : getEventType().hashCode());
            result = prime * result + ((getScene() == null) ? 0 : getScene().hashCode());
            result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
            result = prime * result + ((getIsReserve() == null) ? 0 : getIsReserve().hashCode());
            result = prime * result + ((getTimes() == null) ? 0 : getTimes().hashCode());
            result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
            result = prime * result + ((getDisallowance() == null) ? 0 : getDisallowance().hashCode());

            return result;
        }

        /**
         * This enum was generated by MyBatis Generator.
         * This enum corresponds to the database table dts_region
         *
         * @mbg.generated
         * @project https://github.com/itfsw/mybatis-generator-plugin
         */
        public enum Column {
            id("id", "id", "INTEGER", true),
            userId("userId", "userId", "INTEGER", false),
            userName("userName", "userName", "VARCHAR", true),
            phone("phone", "phone", "VARCHAR", true),
            sex("disallowance", "disallowance", "VARCHAR", true);

//             unit("unit", "unit", "VARCHAR", false);
//            name("name", "name", "VARCHAR", true),
//            type("type", "type", "TINYINT", true),
//            code("code", "code", "INTEGER", false);
//            pid("userId", "userId", "INTEGER", false),
//            name("name", "name", "VARCHAR", true),
//            type("type", "type", "TINYINT", true),
//            code("code", "code", "INTEGER", false);
//            pid("userId", "userId", "INTEGER", false),
//            name("name", "name", "VARCHAR", true),
//            type("type", "type", "TINYINT", true),
//            code("code", "code", "INTEGER", false);


            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private static final String BEGINNING_DELIMITER = "`";

            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private static final String ENDING_DELIMITER = "`";

            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private final String column;

            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private final boolean isColumnNameDelimited;

            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private final String javaProperty;

            /**
             * This field was generated by MyBatis Generator.
             * This field corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            private final String jdbcType;

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String value() {
                return this.column;
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String getValue() {
                return this.column;
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String getJavaProperty() {
                return this.javaProperty;
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String getJdbcType() {
                return this.jdbcType;
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
                this.column = column;
                this.javaProperty = javaProperty;
                this.jdbcType = jdbcType;
                this.isColumnNameDelimited = isColumnNameDelimited;
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String desc() {
                return this.getEscapedColumnName() + " DESC";
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String asc() {
                return this.getEscapedColumnName() + " ASC";
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public static com.qiguliuxing.dts.db.domain.DtsReserveVO.Column[] excludes(com.qiguliuxing.dts.db.domain.DtsReserveVO.Column... excludes) {
                ArrayList<com.qiguliuxing.dts.db.domain.DtsRegion.Column> columns = new ArrayList<>(Arrays.asList(com.qiguliuxing.dts.db.domain.DtsRegion.Column.values()));
                if (excludes != null && excludes.length > 0) {
                    columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
                }
                return columns.toArray(new com.qiguliuxing.dts.db.domain.DtsReserveVO.Column[]{});
            }

            /**
             * This method was generated by MyBatis Generator.
             * This method corresponds to the database table dts_region
             *
             * @mbg.generated
             * @project https://github.com/itfsw/mybatis-generator-plugin
             */
            public String getEscapedColumnName() {
                if (this.isColumnNameDelimited) {
                    return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
                } else {
                    return this.column;
                }
            }
        }
    }

