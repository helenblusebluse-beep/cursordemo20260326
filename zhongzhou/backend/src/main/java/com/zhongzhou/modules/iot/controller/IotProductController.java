package com.zhongzhou.modules.iot.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.iot.service.IotProductService;
import com.zhongzhou.modules.iot.vo.IotProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/iot/products")
public class IotProductController {
    private final IotProductService productService;

    public IotProductController(IotProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<IotProductVO>> list() {
        return ApiResponse.success(productService.listActive());
    }

    /** 同步阿里云产品数据（模拟） */
    @PostMapping("/sync")
    public ApiResponse<Void> sync() {
        productService.syncFromAliyun();
        return ApiResponse.success(null);
    }
}
