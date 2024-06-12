package com.qiguliuxing.dts.wx.util;

import com.qiguliuxing.dts.wx.dao.FreeTimeSolt;
import com.qiguliuxing.dts.wx.dao.UseTimeSolt;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 指定时间范围内的空闲时间段
 * */

public class FreeTime {

    /**
     * useTimeSolt 使用的时间段集合
     * startTime   指定范围开始时间
     * endTime     指定范围结束时间
     * */
    public static List<FreeTimeSolt> freeTime(List<UseTimeSolt> useTimeSolt,LocalTime startTime,LocalTime endTime){
        List<FreeTimeSolt> freeTimeSolts = new ArrayList<>();

        //将起始时间作为第一个空闲时间段的开始时间
        LocalTime currentStartTime = startTime;

        //对于每个已占用时间段，找到与前一个前一个时间段的空闲时间
        for(UseTimeSolt time : useTimeSolt){
            LocalTime useStartTime = time.getUseStartTime();
            LocalTime useEndTime = time.getUseEndTime();

            //如果当前占用时间段的开始时间在前一个空闲时间段之后，创建一个新的空闲时间段
            if (useStartTime.isAfter(currentStartTime)){
                FreeTimeSolt freeTimeSolt = new FreeTimeSolt(currentStartTime,useStartTime);
                freeTimeSolts.add(freeTimeSolt);
            }

            //更新当前空闲时间段的开始时间为当前占用时间段的结束时间
            currentStartTime = useEndTime;
        }
        //如果最后一个占用时间段的结束时间在结束时间之前，创建最后一个空闲时间段
        if (currentStartTime.isBefore(endTime)){
            FreeTimeSolt freeTimeSolt = new FreeTimeSolt(currentStartTime,endTime);
            freeTimeSolts.add(freeTimeSolt);
        }
        return freeTimeSolts;
    }
}

