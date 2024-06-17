package com.qiguliuxing.dts.admin.web;

import com.alibaba.excel.EasyExcel;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.admin.service.SystemManageService;
import com.qiguliuxing.dts.admin.util.AuthSupport;
import com.qiguliuxing.dts.admin.util.Result;
import com.qiguliuxing.dts.admin.util.WechatUtil;
import com.qiguliuxing.dts.core.storage.StorageService;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAd;
import com.qiguliuxing.dts.db.domain.DtsCategory;
import com.qiguliuxing.dts.db.domain.DtsReserve;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.service.DtsAdService;
import com.qiguliuxing.dts.db.service.DtsCategoryService;
import com.qiguliuxing.dts.db.service.DtsUserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/manage")
public class SystemManageController {

    private static final Logger logger = LoggerFactory.getLogger(SystemManageController.class);

    @Autowired
    private StorageService storageService;
    @Autowired
    private DtsAdService adService;
    @Autowired
    private SystemManageService systemManageService;
    @Autowired
    private DtsUserService dtsUserService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WechatUtil wechatUtil;
    @Autowired
    private DtsCategoryService dtsCategoryService;
    /**
     * 轮播图上传
     * */
    //@RequiresPermissions("admin:storage:create")
    //@RequiresPermissionsDesc(menu = { "系统管理", "对象存储" }, button = "上传")
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public Object uploadPicture(@RequestParam("file") MultipartFile file) throws Exception {
        logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 系统管理->对象存储->上传,请求参数,file:{}", file.getOriginalFilename());

        String originalFilename = file.getOriginalFilename();
        String url = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(),originalFilename);
        DtsAd ad = new DtsAd();
        ad.setUrl(url);
        ad.setEnabled(true);
        adService.add(ad);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);

        logger.info("【请求结束】系统管理->对象存储->查询:响应结果:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }


    /**
     * 预约查询
     * */
    @GetMapping("/list")
    public Result<List<DtsReserve>> list(@RequestParam(required = false) String name){
        List<DtsReserve> list = systemManageService.list(name);
        return Result.success(list);
    }

    /**
     * 导出
     * */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        List<DtsReserve> list = systemManageService.all();
        // 这里的 ContentType 要和前端请求携带的 ContentType 相对应
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        //String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="  + "预约报表.xlsx");
        // DownloadData 是实体类，sheet 里面是 sheet 名称，doWrite 里面放要写入的数据，类型为 List<DownloadData>
        EasyExcel.write(response.getOutputStream(), DtsReserve.class).autoCloseStream(Boolean.FALSE).sheet("报表").doWrite(list);
    }


    /**
     * 活动推送
     * */
    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam String theme,@RequestParam String time,@RequestParam String provider,@RequestParam String site,@RequestParam String organ,@RequestParam(required = false) String content){
        String accessToken = wechatUtil.getAccessToken();
        List<DtsUser> all = dtsUserService.all();
        all.stream().forEach(user ->{
            JSONObject body=new JSONObject();
            body.put("touser",user.getWeixinOpenid());
            body.put("template_id","FAapMIqVsN3El4ONaIeHha1B0LHuYkJE4yCzLnCvMvk");
            body.put("page","pages/appointment/line_up");
            Map<String,Object> map = new HashMap<>();
            map.put("thing2",theme);
            map.put("time4",time);
            map.put("thing1",provider);
            map.put("thing3",site);
            map.put("thing7",organ);
            body.put("data",map);
            String result = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, body, String.class);
            System.out.println(result);
        });
        return null;
    }

    /**
     * 预约通道查询
     */
    @GetMapping("/activeList")
    public List<DtsCategory> activeList(){
        List<DtsCategory> dtsCategories = dtsCategoryService.queryChannel();
        return dtsCategories;
    }

    /**
     * 关闭/开启预约
     * */
    @GetMapping("/activeUpdate")
    public Result<String> activeUpdate(@RequestParam String id,@RequestParam boolean isOpen ){
        int update = dtsCategoryService.update(id, isOpen);
        if (update>0){
            return new Result<>(200,"关闭成功");
        }
        return Result.fail("关闭失败");
    }
}
