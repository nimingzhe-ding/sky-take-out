package com.sky.properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upload.dir")
@Data
public class UploadDir {
    // 本地存储的物理路径 (例如: D:/images/)
    private String uploadDir;
    // 浏览器访问的映射路径 (例如: /images/)
    private String uploadPath;
}