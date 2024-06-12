package com.qiguliuxing.dts.db.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DtsReserveMapper extends BaseMapper<DtsReserve> {

    List<DtsReserve> getByDate(@Param("scene")String scene,@Param("date")String date,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
}
