package com.qiguliuxing.dts.admin.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.admin.dao.SubscriberVo;
import com.qiguliuxing.dts.admin.dao.TemplateData;
import com.qiguliuxing.dts.admin.service.SystemManageService;
import com.qiguliuxing.dts.admin.util.*;
import com.qiguliuxing.dts.core.storage.StorageService;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;
import com.qiguliuxing.dts.db.domain.*;
import com.qiguliuxing.dts.db.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private DtsFeedbackService feedbackService;
    @Autowired
    private DtsArticleService articleService;

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
        String result = null;
        for (DtsUser user : all) {
            SubscriberVo subscriberVo = new SubscriberVo();
            subscriberVo.setTouser(user.getWeixinOpenid());
            subscriberVo.setTemplate_id("FAapMIqVsN3El4ONaIeHha1B0LHuYkJE4yCzLnCvMvk");
            subscriberVo.setPage("pages/appointment/line_up");
            Map<String,TemplateData> map = new HashMap<>();
            map.put("thing2",new TemplateData(theme));
            map.put("time4",new TemplateData(time));
            map.put("thing1",new TemplateData(provider));
            map.put("thing3",new TemplateData(site));
            map.put("thing7",new TemplateData(organ));
            subscriberVo.setData(map);
            result = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken,subscriberVo, String.class);
            System.out.println(result);
        }
        return result;
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
    // 留言查询
    @GetMapping("/getMessage")
    public Object getMessage(Integer userId, String username,String type, @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @Sort @RequestParam(defaultValue = "add_time")String sort ,
                             @Order @RequestParam(defaultValue = "desc") String order) {
        logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 用户管理->意见反馈->查询,请求参数:userId:{},username:{},page:{}", userId, username, page);
        //List<DtsFeedback> feedbackList= new ArrayList<>();
        //if (type!=null){
        List<DtsFeedback> feedbackList = feedbackService.all();//userId, username, page, limit, sort, order
                /*if (type.equals("优化建议")){
                    feedbackList = feedbackService.type(type);

                }else if (type.equals("功能异常")){

                    feedbackList = feedbackService.type(type);

                }else {*/
        //查询全部
        //feedbackList = feedbackService.all();//userId, username, page, limit, sort, order
        //}
        //}

        logger.info("【请求结束】留言查看,响应结果:{}", JSONObject.toJSONString(feedbackList));
        return Result.success(feedbackList);
    }

    //活动描述就是公告信息
    @GetMapping("/detail")
    public Object detail(@NotNull Integer id) {
        logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 推广管理->公告管理->详情,请求参数,id:{}", id);
        DtsArticle article = null;
        try {
            article = articleService.findById(id);
        } catch (Exception e) {
            logger.error("获取文章公告失败,文章id：{}", id);
            e.printStackTrace();
        }
        // 这里不打印响应结果，文章内容信息较多
        // logger.info("【请求结束】获取公告文章,响应结果：{}",JSONObject.toJSONString(article));
        return ResponseUtil.ok(article);
    }
    /**
     * 编辑公告
     *
     * @param
     * @return
     */

    @RequiresPermissionsDesc(menu = { "推广管理", "公告管理" }, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody DtsArticle article) {
        logger.info("【请求开始】操作人:[" + AuthSupport.userName()+ "] 推广管理->公告管理->编辑,请求参数:{}", JSONObject.toJSONString(article));
//        Object error = validate(article);
//        if (error != null) {
//            return error;
//        }
        if (StringUtils.isEmpty(article.getType())) {
            article.setType(ArticleType.ANNOUNCE.type());//如果没有传入类型，默认为信息公告
        }
        if (articleService.updateById(article) == 0) {
            logger.error("推广管理->公告管理->编辑错误:{}", "更新数据失败");
            throw new RuntimeException("更新数据失败");
        }
        logger.info("【请求结束】推广管理->公告管理->编辑,响应结果:{}", "成功!");
        return ResponseUtil.ok();
    }

        public static void main(String[] args) throws Exception {
        String path = ResourceUtils.getURL("classpath:").getPath();

        String imagePath = "C:/Users/Asus/Desktop/tushuguan.jpg";
        String logo = path.replace("/dts-wx-api/target/classes/", "/doc/youjianshenfang.png");
        String scene = "图书馆";
        ZXingUtil.encodeimage(imagePath, "JPEG", scene, 430, 430 , logo);
        /**
         * 解密 -->将二维码内部的文字显示出来
         */
        ZXingUtil.decodeImage(new File(imagePath));
    }
}
