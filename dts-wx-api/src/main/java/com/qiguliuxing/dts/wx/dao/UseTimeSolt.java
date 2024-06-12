package com.qiguliuxing.dts.wx.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseTimeSolt {
    LocalTime useStartTime;
    LocalTime useEndTime;
}
