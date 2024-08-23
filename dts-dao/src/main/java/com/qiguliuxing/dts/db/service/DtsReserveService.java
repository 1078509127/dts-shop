package com.qiguliuxing.dts.db.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qiguliuxing.dts.db.dao.DtsReserveMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.util.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DtsReserveService {

   @Resource
   private DtsReserveMapper dtsReserveMapper;
   @Resource
   private DtsUserService dtsUserService;

   public int save(DtsReserve dtsReserve) {
      int insert = dtsReserveMapper.insert(dtsReserve);
      return insert;
   }

   public int update(DtsReserve dtsReserve) {
      LambdaUpdateWrapper<DtsReserve> updateWrapper = new LambdaUpdateWrapper<>();
      updateWrapper.eq(DtsReserve:: getId,dtsReserve.getId());
      updateWrapper.set(DtsReserve::getIsReserve,1);
      return dtsReserveMapper.update(dtsReserve, updateWrapper);
   }

   public List<DtsReserve> list(DtsReserve dtsReserve) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getScene,dtsReserve).eq(DtsReserve::getStartTime,dtsReserve.getStartTime()).eq(DtsReserve::getEndTime,dtsReserve.getEndTime());
      return dtsReserveMapper.selectList(queryWrapper);
   }

   public  int upBydisallowance(DtsReserve dtsReserve){
      LambdaUpdateWrapper<DtsReserve> wrapperHistory = Wrappers.<DtsReserve>update().lambda()
              .in(DtsReserve::getId, dtsReserve.getId())
              .set(DtsReserve::getDisallowance, dtsReserve.getDisallowance());

      // baseMapper写法
      return dtsReserveMapper.update(null, wrapperHistory);
   }



      public List<DtsReserve> getList(String userId,String eventType) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getUserId,userId);
      queryWrapper.eq(DtsReserve::getEventType,eventType);
      queryWrapper.eq(DtsReserve::getIsReserve,0);
      queryWrapper.ge(DtsReserve::getStartTime, DateUtils.getDate());
      return dtsReserveMapper.selectList(queryWrapper);
   }

   public List<DtsReserve> echo(String userId,String eventType) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getUserId,userId);
      queryWrapper.eq(DtsReserve::getEventType,eventType);
      return dtsReserveMapper.selectList(queryWrapper);
   }

   public List<DtsReserve> getByDate(String scene,String date,Date startTime,Date endTime,String tableNumber) {
      return dtsReserveMapper.getByDate(scene,date,startTime,endTime,tableNumber);
   }

   public List<DtsReserve> getByStartAndEnd(String scene,Date startTime,Date endTime) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getScene, scene);
      queryWrapper.gt(DtsReserve::getEndTime, startTime);
      queryWrapper.lt(DtsReserve::getStartTime, endTime);
      queryWrapper.eq(DtsReserve::getIsReserve, 0);
      return dtsReserveMapper.selectList(queryWrapper);
   }

   public List<DtsReserve> getByWeek(Integer userId,Date monday,Date sunday,String scene) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getUserId,userId);
      queryWrapper.ge(DtsReserve::getStartTime,monday);
      queryWrapper.le(DtsReserve::getEndTime,sunday);
      queryWrapper.eq(DtsReserve::getIsReserve,0);
      queryWrapper.eq(DtsReserve::getScene,scene);
      return dtsReserveMapper.selectList(queryWrapper);
   }

   public List<DtsReserve> getMonDateed(String scene,String eventType,String date,String endDate) {
      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      //queryWrapper.eq(DtsReserve::getScene,scene);
      queryWrapper.eq(DtsReserve::getEventType,eventType);
      queryWrapper.ge(DtsReserve::getStartTime,date);
      queryWrapper.le(DtsReserve::getEndTime, endDate);
      return dtsReserveMapper.selectList(queryWrapper);
   }


   public int addOrUpdate(Integer userId, String scene) {
      int flag= 0;
      DtsUser user = dtsUserService.findById(userId);

      LambdaQueryWrapper<DtsReserve> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(DtsReserve::getUserId,userId);
      queryWrapper.eq(DtsReserve::getScene,scene);
      DtsReserve dtsReserve = dtsReserveMapper.selectOne(queryWrapper);
      if (ObjectUtils.isNotEmpty(dtsReserve)){
         Integer times = dtsReserve.getTimes();
         times += 1;
         dtsReserve.setTimes(times);
         dtsReserve.setUserName(user.getUsername());
         dtsReserve.setPhone(user.getMobile());
         flag = dtsReserveMapper.update(dtsReserve, queryWrapper);
      }else {
         DtsReserve reserve = new DtsReserve();
         reserve.setUserId(userId);
         reserve.setScene(scene);
         reserve.setCreateTime(new Date());
         reserve.setIsReserve(0);
         reserve.setEventType("个人预约");
         reserve.setTimes(1);
         reserve.setUserName(user.getUsername());
         reserve.setPhone(user.getMobile());
         flag = dtsReserveMapper.insert(reserve);
      }
      return flag;
   }

   public int delete(Integer userId) {
      int delete = dtsUserService.delete(userId);
      return delete;
   }
}
