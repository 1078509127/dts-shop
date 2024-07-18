package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.db.domain.DtsCategory;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.db.service.DtsCategoryService;
import com.qiguliuxing.dts.db.service.DtsReserveService;
import com.qiguliuxing.dts.db.util.DateUtils;
import com.qiguliuxing.dts.wx.dao.DtsReserveVo;
import com.qiguliuxing.dts.wx.dao.FreeTimeSolt;
import com.qiguliuxing.dts.wx.dao.UseTimeSolt;
import com.qiguliuxing.dts.wx.util.FreeTime;
import com.qiguliuxing.dts.wx.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.qiguliuxing.dts.wx.util.DateCalculator.getMondayOfWeek;
import static com.qiguliuxing.dts.wx.util.DateCalculator.getSundayOfWeek;

/**
 * 活动场所预约
 * */
@RestController
@RequestMapping("/wx/reserve")
public class WxReserveController {

    @Resource
    private DtsReserveService dtsReserveService;
    @Autowired
    private DtsCategoryService dtsCategoryService;
    List<String> Day = new ArrayList<>();

    /**
     *
    团队预约
     */
    @PostMapping("/teamReserve")
    public synchronized Result<Boolean> teamReserve(@RequestBody DtsReserveVo dtsReserveVo) throws ParseException {
        //查询当日是否约满，有预约 空闲时间段剩余哪些
        String start = new StringBuilder().append(dtsReserveVo.getDate()).append(" 00:00:00").toString();
        String end = new StringBuilder().append(dtsReserveVo.getDate()).append(" 00:00:00").toString();
        SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        DtsReserve dtsReserve = new DtsReserve();
        dtsReserve.setStartTime(sdf.parse(start));
        dtsReserve.setEndTime(sdf.parse(end));
        dtsReserve.setIsReserve(0);
        BeanUtils.copyProperties(dtsReserveVo,dtsReserve);
        Result<String> full = this.teamisFull(dtsReserveVo.getScene(), dtsReserveVo.getDate(),dtsReserveVo.getEventType() );
        if (full.getCode() == 500){
            return Result.fail(500,full.getMessage());
        }
        int  save = dtsReserveService.save(dtsReserve);
        if (save>0){
            return new Result<>(200,"预约成功");
        }else {
            return Result.fail("预约失败");
        }
    }

    /**
     团队预约查询
     */

    @GetMapping("/teamisFull")
    public Result<String> teamisFull(@RequestParam String scene,@RequestParam String date,@RequestParam(required = false) String eventType) throws ParseException {
        List<DtsReserve> byDay = dtsReserveService.getByDate(scene,date,null,null);
        if (byDay.isEmpty()){
            return Result.success("可预约");
        }else {
            //有日期已经被预约过

            SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
            Date d =  s.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            // 增加一个月
            calendar.add(Calendar.MONTH, 1);

            Date next= calendar.getTime();
            System.out.print(next);
            String  dateend = s.format(next);
            // 查询已经被预约过的日期
            List<DtsReserve> byDayed =  dtsReserveService.getMonDateed(scene,eventType,date,dateend);
            byDayed.stream().forEach( time ->{

                SimpleDateFormat sDate= new SimpleDateFormat("yyyy-MM-dd");
                String DateDay =  sDate.format(time.getStartTime());
                // 数据库已预约过的日期
                Day.add(DateDay);

            });
            // 预约区间 获取每一天 listDay
            List<String> listDay= new ArrayList<>();
            listDay= getBetweenDate(date,dateend);

            for (int j=0;j<listDay.size();j++){
                for (int i=0;i<Day.size();i++){
                    if (listDay.get(j).contains(Day.get(i))){
                        listDay.remove(j);//得到可预约的日期

                    }
                }

            }
            System.out.println(listDay);
            return Result.fail(500,"只有"+listDay+"时间段可预约");
        }
        //return Result.success("可预约");
    }
    //预约过日期
    //预约过日期
    public  List<String> getBetweenDate(String begin,String end){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> betweenList = new ArrayList<String>();
        Boolean dateFlag= false;
        try{
            Calendar startDay = Calendar.getInstance();
            startDay.setTime(format.parse(begin));
            startDay.add(Calendar.DATE, -1);

            while(true){
                startDay.add(Calendar.DATE, 1);
                Date newDate = startDay.getTime();
                String newend=format.format(newDate);
                betweenList.add(newend);

                if(end.equals(newend)){
                    break;
                }



            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return betweenList;
    }
    /**
     * 预约
     * */
    @PostMapping("/reserve")
    public synchronized Result<Boolean> reserve(@RequestBody DtsReserveVo dtsReserveVo) throws ParseException {
        //查询当日是否约满，有预约 空闲时间段剩余哪些
        String start = new StringBuilder().append(dtsReserveVo.getDate()).append(" ").append(dtsReserveVo.getStartTime()).toString();
        String end = new StringBuilder().append(dtsReserveVo.getDate()).append(" ").append(dtsReserveVo.getEndTime()).toString();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        DtsReserve dtsReserve = new DtsReserve();
        dtsReserve.setStartTime(sdf.parse(start));
        dtsReserve.setEndTime(sdf.parse(end));
        dtsReserve.setIsReserve(0);
        dtsReserve.setCreateTime(new Date());
        BeanUtils.copyProperties(dtsReserveVo,dtsReserve);
        Result<String> full = this.isFull(dtsReserveVo.getScene(),dtsReserveVo.getUserId(), dtsReserveVo.getDate(), dtsReserveVo.getStartTime()+":00", dtsReserveVo.getEndTime()+":00");
        if (full.getCode() == 500){
            return Result.fail(500,full.getMessage());
        }
        int  save = dtsReserveService.save(dtsReserve);
        if (save>0){
            return new Result<>(200,"预约成功");
        }else {
            return Result.fail("预约失败");
        }
    }

    /**
     * 取消预约
     * */
    @PostMapping("delReserve")
    public Object delReserve(@RequestBody DtsReserveVo dtsReserveVo) throws ParseException {
        String start = new StringBuilder().append(dtsReserveVo.getDate()).append(" ").append(dtsReserveVo.getStartTime()).toString();
        String end = new StringBuilder().append(dtsReserveVo.getDate()).append(" ").append(dtsReserveVo.getEndTime()).toString();
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        DtsReserve dtsReserve = new DtsReserve();
        dtsReserve.setStartTime(sdf.parse(start));
        dtsReserve.setEndTime(sdf.parse(end));
        BeanUtils.copyProperties(dtsReserveVo,dtsReserve);
        int update = dtsReserveService.update(dtsReserve);
        if (update>0){
            return new Result<>(200,"取消成功");
        }else {
            return Result.fail("取消失败");
        }
    }

    /**
     * 查询预约
     * */
    @GetMapping("/selReserve")
    public Object selReserve(@RequestParam(required = true) String userId,@RequestParam(required = false) String eventType) throws ParseException {
        List<DtsReserve> dtsReserves = dtsReserveService.getList(userId,eventType);
        List<DtsReserveVo> dtsReserveVos = new ArrayList<>();
        dtsReserves.stream().forEach(dtsReserve -> {
            DtsReserveVo dtsReserveVo = new DtsReserveVo();
            BeanUtils.copyProperties(dtsReserve,dtsReserveVo);

            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = date.format(dtsReserve.getStartTime());
            dtsReserveVo.setDate(dateStr);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startStr = sdf.format(dtsReserve.getStartTime());
            dtsReserveVo.setStartTime(startStr);
            String endStr = sdf.format(dtsReserve.getEndTime());
            dtsReserveVo.setEndTime(endStr);

            dtsReserveVos.add(dtsReserveVo);
        });
        return new Result<>(200,null,dtsReserveVos);
    }

    @GetMapping("/isFull")
    public Result<String> isFull(@RequestParam String scene,@RequestParam Integer userId,@RequestParam String date,@RequestParam(required = false) String startTime,@RequestParam(required = false) String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dates = sdf.parse(date);
        Date monday = getMondayOfWeek(dates);
        Date sunday = getSundayOfWeek(dates);
        List<DtsReserve> byWeek = dtsReserveService.getByWeek(userId,monday, sunday);
        if (byWeek.size() >= 2){
            return Result.fail(500,"每周至多可预约两次");
        }
        //查询选择日是否约满，约满直接返回fail
        FreeTime freeTime = new FreeTime();
        List<UseTimeSolt> useList = new ArrayList<>();
        LocalTime openTime = LocalTime.of(15, 0);//开放时间
        LocalTime closeTime = LocalTime.of(20, 0);//关闭时间

        //查询当日是否有预约，无预约直接返回，有预约判断是否约满、预约时间段和库中数据是否有时间重叠
        List<DtsReserve> byDay = dtsReserveService.getByDate(scene,date,null,null);
        if (byDay.isEmpty()){
            return Result.success("可预约");
        }
        byDay.stream().forEach(useTime ->{
            LocalTime startLocalTime = useTime.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endLocalTime = useTime.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            UseTimeSolt useTimeSolt = new UseTimeSolt(startLocalTime,endLocalTime);
            useList.add(useTimeSolt);
        });
        Collections.sort(useList, new UseTimeSoltComparator());//这里必须排序，否则freeTime无效
        List<FreeTimeSolt> freeTimeSolts = freeTime.freeTime(useList, openTime,closeTime);
        if (freeTimeSolts.isEmpty()){
            return Result.fail(500,"当日无可预约时间段");
        }
        if (!Objects.isNull(startTime) && !Objects.isNull(endTime)){
            boolean overlapping = checkForOverlap(LocalTime.parse(startTime,DateTimeFormatter.ofPattern("HH:mm:ss")), LocalTime.parse(endTime,DateTimeFormatter.ofPattern("HH:mm:ss")), useList);
            if (overlapping){
                StringBuilder str = new StringBuilder();
                freeTimeSolts.forEach(freeTimeSolt -> {
                    str.append(freeTimeSolt.getFreeStartTime()).append("-").append(freeTimeSolt.getFreeEndTime()).append(",");
                });
                return Result.fail(500,"只有"+str.substring(0, str.length() - 1)+"时间段可预约");
            }
        }
        return Result.success("可预约");
    }

    static class UseTimeSoltComparator implements Comparator<UseTimeSolt> {
        @Override
        public int compare(UseTimeSolt timeSlot1, UseTimeSolt timeSlot2) {
            // 比较开始时间
            return timeSlot1.getUseStartTime().compareTo(timeSlot2.getUseStartTime());
        }
    }

    public static boolean checkForOverlap(LocalTime checkStartTime, LocalTime checkEndTime, List<UseTimeSolt> timeSlots) {
        if (!Objects.isNull(checkStartTime) && !Objects.isNull(checkEndTime)){
            for (UseTimeSolt timeSlot : timeSlots) {
                // 检查时间段是否有重叠
                if (checkStartTime.isBefore(timeSlot.getUseEndTime()) && checkEndTime.isAfter(timeSlot.getUseStartTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询具体时间段被使用的乒乓球桌号
     * */
    @GetMapping("/getTableList")
    public Result<List<String>> getTableList(@RequestParam String scene,@RequestParam String date,@RequestParam(required = false) String startTime,@RequestParam(required = false) String endTime){
        Date startDate = DateUtils.parseDate(date+ " " + startTime);
        Date endDate = DateUtils.parseDate(date+ " " + endTime);
        List<DtsReserve> byStartAndEnd = dtsReserveService.getByStartAndEnd(scene, startDate, endDate);
        List<String> list = new ArrayList<>();
        byStartAndEnd.stream().forEach(dtsReserve ->{
            list.add(dtsReserve.getTableNumber());
        });
        return Result.success(list);
    }

    /**
     * 获取预约活动列表
     * */
    @GetMapping("/activeList")
    public List<DtsCategory> activeList(){
        List<DtsCategory> dtsCategories = dtsCategoryService.queryChannel();
        return dtsCategories;
    }

    /**
     * 微信扫一扫调用接口
     * 健身房/图书馆使用次数
     * */
    @GetMapping("/scan")
    public Result scan(@RequestParam Integer userId,@RequestParam String scene){
        int i = dtsReserveService.addOrUpdate(userId, scene);
        if (i>0){
            return new Result(200,"扫码成功");
        }else {
            return new Result(400,"扫码失败");
        }
    }
}
