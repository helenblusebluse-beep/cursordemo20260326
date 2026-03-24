package com.example.zhulong.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 头像存储：默认本机目录；仅当 {@link #sftpEnabled} 为 true 时走 SFTP。
 */
@Data
@ConfigurationProperties(prefix = "app.avatar")
public class AvatarProperties {

    /**
     * 为 true 时使用 SFTP 上传（需配置 sftp 与 {@link #publicUrlPrefix}）。
     * 默认为 false：头像保存到本机 {@link #localDirectory}，不依赖远程 SSH。
     */
    private boolean sftpEnabled = false;

    /** 本机头像保存目录（相对工作目录或绝对路径） */
    private String localDirectory = "data/avatar-uploads";

    /** 返回给前端的可访问 URL 前缀，须与 WebMvc 静态映射一致 */
    private String localUrlPrefix = "/uploads/avatars/";

    private Sftp sftp = new Sftp();

    /** 仅 sftpEnabled=true 时：浏览器中 &lt;img src&gt; 使用的完整 URL 前缀 */
    private String publicUrlPrefix = "http://192.168.81.152/uploads/";

    @Data
    public static class Sftp {
        private String host = "192.168.81.152";
        private int port = 22;
        private String username = "root";
        private String password = "";
        /** 远程保存目录，如 /tmp */
        private String remoteDirectory = "/tmp";
    }
}
