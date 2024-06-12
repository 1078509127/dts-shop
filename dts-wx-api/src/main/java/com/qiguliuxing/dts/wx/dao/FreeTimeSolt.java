package com.qiguliuxing.dts.wx.dao;

import lombok.Data;

import java.time.LocalTime;

@Data
public class FreeTimeSolt {
    LocalTime freeStartTime;
    LocalTime freeEndTime;

    public FreeTimeSolt(LocalTime freeStartTime,LocalTime freeEndTime){
        this.freeStartTime = freeStartTime;
        this.freeEndTime = freeEndTime;
    }
}
