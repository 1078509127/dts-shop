package com.qiguliuxing.dts.wx.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.qiguliuxing.dts.core.storage.StorageService;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;
import com.qiguliuxing.dts.db.domain.*;
import com.qiguliuxing.dts.db.service.*;
import com.qiguliuxing.dts.wx.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.wx.dao.DtsRreserveVOVO;
import com.qiguliuxing.dts.wx.dao.SubscriberVo;
import com.qiguliuxing.dts.wx.dao.TemplateData;
import com.qiguliuxing.dts.wx.service.SystemManageService;
import com.qiguliuxing.dts.wx.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/wx/manage")
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
    @Autowired
    private DtsUserService userService;
    @Value("${dts.wx.msg_sec_check}")
    private String msgSecCheck;
    @Value("${dts.wx.app-id}")
    private String appId;

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
    public void download(HttpServletResponse response, @RequestParam String date ) throws IOException {
        // 这里的 ContentType 要和前端请求携带的 ContentType 相对应
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename="  + "预约报表.xlsx");

        List<String> string1 = Arrays.asList(new String[]{"乒乓球馆", "微机室", "瑜伽室", "书法室", "录音室", "烘培室"});
        List<DtsReserve> list1 = systemManageService.getData(date,string1);

        List<DtsRreserveVOVO> dtsReserveVo = new ArrayList<>();

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdffTmie =new SimpleDateFormat("HH:mm:ss");

        if(list1.size()>0){
            for (int i=0;i<list1.size();i++){
                DtsRreserveVOVO dtsReserveVo1 = new DtsRreserveVOVO();
                BeanUtils.copyProperties(list1.get(i),dtsReserveVo1);
                String date1 =sdfDate.format(list1.get(i).getCreateTime());
                String date2 =sdffTmie.format(list1.get(i).getCreateTime());
                dtsReserveVo1.setReserveDate(date1);//开始日期
                dtsReserveVo1.setReservetime(date2);//开始时间

                dtsReserveVo.add(dtsReserveVo1);

            }
        }


        //去除指定列
        List<String> excludeColumnNames = new ArrayList<>();
        //列宽度map
        Map<Integer, Integer> columnWidthMap = new HashMap<>();


        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
        WriteSheet sheet1 = EasyExcel.writerSheet(0, "报表1").head(DtsRreserveVOVO.class).excludeColumnFiledNames(excludeColumnNames).build();
        sheet1.setColumnWidthMap(columnWidthMap);
        excelWriter.write(dtsReserveVo, sheet1);

        List<String> string2 = Arrays.asList(new String[]{"健身房", "图书馆"});
        List<DtsReserve> list2 = systemManageService.getData(date,string2);
        List<DtsRreserveVOVO> dtsReserveVoList = new ArrayList<>();

        if(list2.size()>0){
            for (int j=0;j<list2.size();j++){
                DtsRreserveVOVO dtsReserveVo2 = new DtsRreserveVOVO();
                BeanUtils.copyProperties(list2.get(j),dtsReserveVo2);
                String date1 =sdfDate.format(list2.get(j).getCreateTime());
                String date2 =sdffTmie.format(list2.get(j).getCreateTime());
                dtsReserveVo2.setReserveDate(date1);//开始日期
                dtsReserveVo2.setReservetime(date2);//开始时间

                dtsReserveVoList.add(dtsReserveVo2);

            }
        }
        //只包含指定列
        List<String> includeColumnNames  = new ArrayList<>();
        includeColumnNames.add("userId");
        includeColumnNames.add("userName");
        includeColumnNames.add("phone");
//        includeColumnNames.add("eventType"); //个人预约/团队预约
        includeColumnNames.add("scene");

        includeColumnNames.add("times");
        includeColumnNames.add("reservetime");
        includeColumnNames.add("reserveDate");


        WriteSheet sheet2 = EasyExcel.writerSheet(1, "报表2").head(DtsRreserveVOVO.class).includeColumnFiledNames(includeColumnNames).build();

        sheet2.setColumnWidthMap(columnWidthMap);
        excelWriter.write(dtsReserveVoList, sheet2);
        excelWriter.finish();
    }



    /**
     * 活动推送
     * */
    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam String theme,@RequestParam String time,@RequestParam String provider,@RequestParam String site,@RequestParam String organ,@RequestParam(required = false) String content){
        String accessToken = wechatUtil.getAccessToken();
        List<DtsUser> all = dtsUserService.all();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (DtsUser user : all) {
            executor.execute(() -> {
                // 这里编写线程执行的任务逻辑
                String msg = wechatUtil.getMsg(user,theme, time, provider, site, organ, accessToken);
                System.out.println("msg===>"+msg);
            });
        /*CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String msg = wechatUtil.getMsg(user,theme, time, provider, site, organ, accessToken);
            System.out.println("msg===>"+msg);
            return msg;
        }, executor);
        futures.add(future);*/
        }
        // 等待所有 CompletableFuture 完成并获取结果
    /*List<String> results = new ArrayList<>();
    for (CompletableFuture<String> future : futures) {
        try {
            results.add(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
        executor.shutdown();
        //System.out.println("results===>"+results);
        return "";
    }

    /**
     * 消息内容安全检查
     * @param content
     * @return
     */
    @GetMapping("/msgSecCheck")
    public Boolean msgSecCheck(@RequestParam String content){
        String accessToken = wechatUtil.getAccessToken();
        JSONObject data = new JSONObject();
        data.put("openid", appId);
        data.put("scene", 1);
        data.put("version",1);
        data.put("content", content);
        String result = restTemplate.postForObject(msgSecCheck+"?access_token="+accessToken, data.toString(),String.class);
        if (result.contains("ok")){
            return true;
        }
        return false;
    }
    /**
     * 检查图像
     *	<p><a href="https://developers.weixin.qq.com/miniprogram/dev/framework/security.imgSecCheck.html">校验一张图片是否含有违法违规内容</a>
     * @param file 文件
     * @return {@link R}<{@link Boolean}>
     */
   /* @SneakyThrows(Exception.class)
    @PostMapping("imgSecCheck")
    @ApiOperation(value = "微信图片审核", notes = "传入content")
    public R<Boolean> checkImage(MultipartFile file) {
        //TODO 校验文件类型
        File fileTemp = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + ".tmp");
        FileUtils.copyInputStreamToFile(file.getInputStream(), fileTemp);
        String httpUrl = "https://api.weixin.qq.com/wxa/img_sec_check" + "?access_token=" + wxService.getAccessToken();
        HttpPost httpPost = new HttpPost(httpUrl);
        HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("media", fileTemp).setMode(HttpMultipartMode.RFC6532).build();
        httpPost.setEntity(entity);
        CloseableHttpClient client = DefaultApacheHttpClientBuilder.get().build();
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        String responseContent = (String) Utf8ResponseHandler.INSTANCE.handleResponse(httpResponse);
        JSONObject resp = JSONUtil.parseObj(responseContent);
        if (resp.getInt("errcode") == 0) {
            return R.success("安全审查通过");
        }
        fileTemp.delete();
        return R.fail("安全审查未通过");
    }*/


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
        DtsUser user =null;
        DtsFeedback d =null;

        for (int i=0;i<feedbackList.size();i++){

            user = userService.findById(feedbackList.get(i).getUserId());
            if (user !=null){

                feedbackList.get(i).setUsername(user.getUsername());

                //d.setUsername(user.getUsername());
                //feedbackMapper.updateByPrimaryKey(d);
            }


        }



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

    @GetMapping("/QRcode")
    public void QRcode(@RequestParam String scene,HttpServletResponse response) throws IOException, WriterException {
        // 设置响应流信息
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream stream = response.getOutputStream();

        //String scene = "图书馆";
        String path = ResourceUtils.getURL("classpath:").getPath();
        String logo = path.replace("/dts-wx-api/target/classes/", "/doc/youjianshenfang.png");
        BitMatrix encodeimage = ZXingUtil.encodeimage("imagePath", "JPEG", scene, 430, 430, logo);

        //以流的形式输出到前端
        MatrixToImageWriter.writeToStream(encodeimage , "jpg" , stream);
    }
}
