package com.qiguliuxing.dts.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiguliuxing.dts.db.dao.DtsReserveMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.db.service.DtsReserveService;
import com.qiguliuxing.dts.db.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DtsReserveServiceImpl  implements DtsReserveService {

    @Resource
    private DtsReserveMapper dtsReserveMapper;

    @Override
    public int save(DtsReserve dtsReserve) {
        int insert = dtsReserveMapper.insert(dtsReserve);
        return insert;
    }

    @Override
    public int update(DtsReserve dtsReserve) {
        LambdaUpdateWrapper<DtsReserve> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DtsReserve:: getUserId,dtsReserve.getUserId()).eq(DtsReserve::getScene,dtsReserve.getScene())
                    .eq(DtsReserve::getStartTime,dtsReserve.getStartTime()).eq(DtsReserve::getEndTime,dtsReserve.getEndTime());
        updateWrapper.set(DtsReserve::getIsReserve,1);
        return dtsReserveMapper.update(dtsReserve, updateWrapper);
    }

    @Override
    public List<DtsReserve> list(DtsReserve dtsReserve) {
        LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DtsReserve::getScene,dtsReserve).eq(DtsReserve::getStartTime,dtsReserve.getStartTime()).eq(DtsReserve::getEndTime,dtsReserve.getEndTime());
        return dtsReserveMapper.selectList(queryWrapper);
    }

    @Override
    public List<DtsReserve> getList(String userId) {
        LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DtsReserve::getUserId,userId);
        queryWrapper.eq(DtsReserve::getIsReserve,0);
        queryWrapper.ge(DtsReserve::getStartTime,DateUtils.getDate());
        return dtsReserveMapper.selectList(queryWrapper);
    }

    @Override
    public List<DtsReserve> getByDate(String scene,String date,Date startTime,Date endTime) {
        return dtsReserveMapper.getByDate(scene,date,startTime,endTime);
    }
}
