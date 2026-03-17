package com.nbcb.pay.controller;

import com.nbcb.pay.service.DiscountService;
import com.nbcb.pay.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {

    public record DiscountValidateRequest(
            String code,
            BigDecimal orderAmount,
            BigDecimal taxRate
    ) {
    }

    public record DiscountValidateResponse(
            boolean valid,
            String message,
            BigDecimal discountAmount,
            BigDecimal finalAmount,
            BigDecimal subtotal,
            BigDecimal taxableAmount,
            BigDecimal taxAmount,
            BigDecimal totalAmount
    ) {
    }

    private final DiscountService discountService;
    private final PricingService pricingService;

    public DiscountController(DiscountService discountService, PricingService pricingService) {
        this.discountService = discountService;
        this.pricingService = pricingService;
    }

    @PostMapping("/validate")
    public ResponseEntity<DiscountValidateResponse> validate(@RequestBody DiscountValidateRequest request) {
        BigDecimal orderAmount = request.orderAmount() == null ? BigDecimal.ZERO : request.orderAmount();
        BigDecimal taxRate = request.taxRate() == null ? BigDecimal.ZERO : request.taxRate();

        var discountResult = discountService.applyDiscount(orderAmount, request.code());
        var pricingResult = pricingService.calculateTotal(orderAmount, taxRate, request.code());

        DiscountValidateResponse response = new DiscountValidateResponse(
                discountResult.valid(),
                discountResult.message(),
                discountResult.discountAmount(),
                discountResult.finalAmount(),
                pricingResult.subtotal(),
                pricingResult.discountAmount(),
                pricingResult.taxableAmount(),
                pricingResult.taxAmount(),
                pricingResult.totalAmount()
        );

        return ResponseEntity.ok(response);
    }
}

