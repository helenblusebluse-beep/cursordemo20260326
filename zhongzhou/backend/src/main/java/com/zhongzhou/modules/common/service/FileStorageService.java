package com.zhongzhou.modules.common.service;

import com.zhongzhou.modules.common.vo.UploadedFileVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    public UploadedFileVO storeNursingImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("图片不能为空");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = getExt(original);
        if (!isImageExt(ext)) {
            throw new IllegalArgumentException("仅支持上传PNG/JPG/JPEG类型图片");
        }
        if (file.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("图片大小不超过5M");
        }
        String ym = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String saveName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path dir = Paths.get(uploadDir, "nursing", ym).toAbsolutePath().normalize();
        try {
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(saveName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("上传失败，请稍后重试");
        }
        UploadedFileVO vo = new UploadedFileVO();
        vo.setFileName(original);
        vo.setFileUrl("/uploads/nursing/" + ym + "/" + saveName);
        return vo;
    }

    private String getExt(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx < 0 ? "" : fileName.substring(idx).toLowerCase(Locale.ROOT);
    }

    private boolean isImageExt(String ext) {
        return ".png".equals(ext) || ".jpg".equals(ext) || ".jpeg".equals(ext);
    }
}
