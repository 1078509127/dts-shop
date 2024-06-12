package com.qiguliuxing.dts.db.service;


import com.qiguliuxing.dts.db.domain.DtsReserve;

import java.util.Date;
import java.util.List;

public interface DtsReserveService {

   int save(DtsReserve dtsReserve);

   int update(DtsReserve dtsReserve);

   List<DtsReserve> list(DtsReserve dtsReserve);

   List<DtsReserve> getList(String userId);

   List<DtsReserve> getByDate(String scene,String date,Date startTime,Date endTime);
}
