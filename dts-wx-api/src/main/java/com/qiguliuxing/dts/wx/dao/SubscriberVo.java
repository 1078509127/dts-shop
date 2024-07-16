package com.qiguliuxing.dts.wx.dao;

import lombok.Data;

import java.util.Map;

@Data
public class SubscriberVo {

    private String touser;

    private String template_id;

    private String page;

    private Map<String,TemplateData> data;

}
