package com.qiguliuxing.dts.wx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qiguliuxing.dts.db.dao.SystemManageExcelMapper;
import com.qiguliuxing.dts.db.dao.SystemManageMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.wx.dao.DtsRreserveVOVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SystemManageService {

    @Autowired
    private SystemManageMapper systemManageMapper;
    @Autowired
    private SystemManageExcelMapper systemManageExcelMapper;

    public List<DtsReserve> all(){
        LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
        return systemManageMapper.selectList(queryWrapper);
    }

    public List<DtsReserve> list(String name){
        return systemManageMapper.list(name);
    }

    public List<DtsReserve> getData(String date,List<String> list){
        LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DtsReserve::getIsReserve,0);
        queryWrapper.in(DtsReserve::getScene,list);
        //queryWrapper.ne(DtsReserve::getDisallowance,'1');
        queryWrapper.isNull(DtsReserve::getDisallowance);//驳回不查询
        queryWrapper.orderByDesc(DtsReserve::getCreateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (date.equals("近三个月")){
            calendar.add(Calendar.MONTH, -3);
            Date dBefore = calendar.getTime();
            queryWrapper.ge(DtsReserve::getCreateTime,dBefore);
            queryWrapper.le(DtsReserve::getCreateTime,new Date());
        }
        if (date.equals("近半年")){
            calendar.add(Calendar.MONTH, -6);
            Date dBefore = calendar.getTime();
            queryWrapper.ge(DtsReserve::getCreateTime,dBefore);
            queryWrapper.le(DtsReserve::getCreateTime,new Date());
        }
        if (date.equals("近一年")){
            calendar.add(Calendar.MONTH, -12);
            Date dBefore = calendar.getTime();
            queryWrapper.ge(DtsReserve::getCreateTime,dBefore);
            queryWrapper.le(DtsReserve::getCreateTime,new Date());
        }
        return systemManageMapper.selectList(queryWrapper);
    }


}
