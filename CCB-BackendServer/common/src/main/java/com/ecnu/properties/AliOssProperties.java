package com.ecnu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ccb.alioss")
@Data
public class AliOssProperties {

    /**
     * 阿里云服务相关配置属性，用于存储图片
     */
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
