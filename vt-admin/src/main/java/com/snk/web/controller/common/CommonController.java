package com.snk.web.controller.common;

import com.snk.common.config.Global;
import com.snk.common.config.ServerConfig;
import com.snk.common.constant.Constants;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.file.FileUploadUtils;
import com.snk.common.utils.file.FileUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import zkteco.id100com.jni.id100sdk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求处理
 * 
 * @author snk
 */
@Controller
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.isValidFilename(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = Global.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = Global.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // 本地资源路径
        String localPath = Global.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

    /**
     * 拍摄
     * @return
     */
    @GetMapping("/common/photograph/{imgId}")
    public String photograph(@PathVariable("imgId") String imgId, ModelMap mmap) {
        mmap.put("imgId", imgId);
        return "/common/photograph";
    }

    /**
     * 读身份证
     */
    @GetMapping("/common/readIdCard")
    @ResponseBody
    public AjaxResult readIdCard() {
        if(id100sdk.InitCommExt() <= 0)
        {
            return AjaxResult.error("无法连接到身份证阅读器");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(id100sdk.Authenticate() != 1)
        {
            return AjaxResult.error("未检测到身份证");
        }
        // 循环读取
        long tickStart = System.currentTimeMillis();
        int ret = 0;
        while((ret = id100sdk.ReadContent(1)) != 1 && System.currentTimeMillis() - tickStart < 2000)
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (ret != 1)
        {
            return AjaxResult.error("读取身份证信息失败");
        }
        Map<String, String> map = new HashMap<>();
        // 解析身份证照片
        id100sdk.GetBmpPhotoExt();
        map.put("name", id100sdk.getName());
        map.put("nation", id100sdk.getNationCode() + "");
        map.put("sex", id100sdk.getSexCode() + "");
        map.put("idNum", id100sdk.getIDNum());
        map.put("birthdate", id100sdk.getBirthdate());
        map.put("address", id100sdk.getAddress());
        map.put("issue", id100sdk.getIssue());
        map.put("expDate", id100sdk.getEffectedDate() + "-" + id100sdk.getExpireDate());
        map.put("photo", id100sdk.getJPGPhotoBase64());
        return AjaxResult.success(map);
    }

}
