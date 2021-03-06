package com.hxshop.service.impl;

import com.hxshop.common.util.FtpUtil;
import com.hxshop.common.util.IDUtils;
import com.hxshop.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class PictureServiceImpl implements PictureService {
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASEPATH}")
    private String FTP_BASEPATH;
    @Value("${IMAGE_URL}")
    private String IMAGE_URL;
    @Override
    public Map uploadPicture(MultipartFile file) {
        Map resultMap=new HashMap();
        try
        {
        String oldName=file.getOriginalFilename();
        String newName= IDUtils.genImageName()+oldName.substring(oldName.lastIndexOf("."));
        String filePath=new DateTime().toString("/yyyy/MM/dd");

        boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASEPATH, filePath, newName, file.getInputStream());
        if(!result)
        {
            resultMap.put("error",1);
            resultMap.put("message","图片上传失败！");
            return resultMap;
        }
        resultMap.put("error",0);
        resultMap.put("url",IMAGE_URL+filePath+"/"+newName);
        return resultMap;
        }
        catch (IOException e) {
            resultMap.put("error", 1);
            resultMap.put("message", "图片上传异常");
            return resultMap;
        }
    }
}
