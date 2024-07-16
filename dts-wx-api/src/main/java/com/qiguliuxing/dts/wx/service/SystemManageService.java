package com.qiguliuxing.dts.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiguliuxing.dts.db.dao.SystemManageMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemManageService {

    @Autowired
    private SystemManageMapper systemManageMapper;

    public List<DtsReserve> all(){
        LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
        return systemManageMapper.selectList(queryWrapper);
    }

    public List<DtsReserve> list(String name){

        return systemManageMapper.list(name);
    }
}
