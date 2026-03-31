package com.zhongzhou.modules.common.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.common.service.FileStorageService;
import com.zhongzhou.modules.common.vo.UploadedFileVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UploadedFileVO> uploadImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        UploadedFileVO vo = fileStorageService.storeNursingImage(file);
        String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        vo.setFileUrl(base + vo.getFileUrl());
        return ApiResponse.success(vo);
    }
}
