package com.sky.controller.admin;

import com.sky.properties.UploadDir;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
        @Autowired
        private UploadDir uploadDir;
        @RequestMapping("/upload")
        @ApiOperation("文件上传")
        public Result<String> upload(MultipartFile file){
            log.info("文件上传：{}",file);
            try{
                //获取原始文件名
                String fileName = file.getOriginalFilename();
                //获取后缀名
                String extensionName = fileName.substring(fileName.lastIndexOf("."));
                //构建新文件名（防止重名）
                String newFileName = UUID.randomUUID().toString()+extensionName;
                    log.info("配置的本地存储路径 uploadDir: {}", uploadDir.getUploadDir());

                String filePath = uploadDir.getUploadDir()+ newFileName;
                File localFile = new File(filePath);
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
                file.transferTo(localFile);
                String url = "http://localhost:8080" + uploadDir.getUploadPath() + newFileName;
                return Result.success(url);
            }catch (Exception e){

                log.error("文件上传失败：{},e");

                e.printStackTrace();
            }

            return Result.error("文件");
        }
}
