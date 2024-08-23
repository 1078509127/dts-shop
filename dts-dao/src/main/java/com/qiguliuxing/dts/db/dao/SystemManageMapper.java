package com.qiguliuxing.dts.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.db.domain.DtsReserveVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemManageMapper extends BaseMapper<DtsReserve> {

    List<DtsReserve> list(@Param("name") String name);
}
