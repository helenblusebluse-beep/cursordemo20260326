package com.example.zhulong.employee;

import com.example.zhulong.config.AvatarProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarSftpUploadService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg", "image/jpg");
    private static final int MAX_BYTES = 2 * 1024 * 1024;

    private final AvatarProperties avatarProperties;

    public AvatarSftpUploadService(AvatarProperties avatarProperties) {
        this.avatarProperties = avatarProperties;
    }

    /**
     * 校验图片；默认写入本机目录，仅当 app.avatar.sftp-enabled=true 时使用 SFTP。
     */
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("请选择图片文件");
        }
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !ALLOWED_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("仅支持 PNG/JPG/JPEG 图片");
        }
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (Exception e) {
            throw new IllegalArgumentException("读取文件失败: " + e.getMessage());
        }
        if (bytes.length > MAX_BYTES) {
            throw new IllegalArgumentException("图片大小不能超过2M");
        }
        String ext = extensionForContentType(contentType);
        String filename = "avatar-" + UUID.randomUUID().toString().replace("-", "") + ext;

        if (!avatarProperties.isSftpEnabled()) {
            return saveToLocalDisk(bytes, filename);
        }

        try {
            AvatarProperties.Sftp sftp = avatarProperties.getSftp();
            if (!StringUtils.hasText(sftp.getPassword())) {
                throw new IllegalArgumentException("已开启 SFTP（app.avatar.sftp-enabled=true）但未配置密码，请设置 AVATAR_SFTP_PASSWORD，或关闭 sftp-enabled 使用本机存储");
            }
            uploadBytes(bytes, filename, sftp);
            return normalizePublicUrl(avatarProperties.getPublicUrlPrefix(), filename);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("头像上传失败（SFTP）: " + e.getMessage());
        }
    }

    private static String extensionForContentType(String contentType) {
        String ct = contentType.toLowerCase(Locale.ROOT);
        if (ct.contains("png")) {
            return ".png";
        }
        return ".jpg";
    }

    private String saveToLocalDisk(byte[] bytes, String filename) {
        try {
            Path base = Paths.get(avatarProperties.getLocalDirectory()).toAbsolutePath().normalize();
            Files.createDirectories(base);
            Path target = base.resolve(filename).normalize();
            if (!target.startsWith(base)) {
                throw new IllegalArgumentException("非法文件名");
            }
            Files.write(target, bytes);
            return normalizePublicUrl(avatarProperties.getLocalUrlPrefix(), filename);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("头像保存失败: " + e.getMessage());
        }
    }

    private static String normalizePublicUrl(String prefix, String filename) {
        if (!StringUtils.hasText(prefix)) {
            return filename;
        }
        String p = prefix.trim();
        if (!p.endsWith("/")) {
            p = p + "/";
        }
        return p + filename;
    }

    private void uploadBytes(byte[] bytes, String remoteFileName, AvatarProperties.Sftp cfg) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channel = null;
        try {
            session = jsch.getSession(cfg.getUsername(), cfg.getHost(), cfg.getPort());
            session.setPassword(cfg.getPassword());
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(15_000);

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(15_000);
            String dir = cfg.getRemoteDirectory().trim();
            if (!dir.startsWith("/")) {
                dir = "/" + dir;
            }
            cdOrMkdirs(channel, dir);
            try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                channel.put(in, remoteFileName, ChannelSftp.OVERWRITE);
            }
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private static void cdOrMkdirs(ChannelSftp channel, String dir) throws SftpException {
        String[] parts = dir.split("/");
        if (parts.length == 0) {
            return;
        }
        channel.cd("/");
        StringBuilder path = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            path.append("/").append(part);
            String segment = path.toString();
            try {
                channel.cd(segment);
            } catch (SftpException e) {
                channel.mkdir(segment);
                channel.cd(segment);
            }
        }
    }
}
