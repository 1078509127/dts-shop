package com.qiguliuxing.dts.db.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtsFeendbackFindUser {


    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.username
     *
     * @mbg.generated
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.mobile
     *
     * @mbg.generated
     */
    private String mobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.feed_type
     *
     * @mbg.generated
     */
    private String feedType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.has_picture
     *
     * @mbg.generated
     */
    private Boolean hasPicture;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.pic_urls
     *
     * @mbg.generated
     */
    private String[] picUrls;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.add_time
     *
     * @mbg.generated
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column dts_feedback.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;




 }
