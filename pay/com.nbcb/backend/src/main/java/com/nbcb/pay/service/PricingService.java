package com.nbcb.pay.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public record PricingResult(
            BigDecimal subtotal,
            BigDecimal discountAmount,
            BigDecimal taxableAmount,
            BigDecimal taxAmount,
            BigDecimal totalAmount
    ) {
    }

    private final DiscountService discountService;

    public PricingService(DiscountService discountService) {
        this.discountService = discountService;
    }

    /**
     * 在税前应用折扣，然后基于折后金额计算税费。
     *
     * @param subtotal     商品小计（税前、未折扣）
     * @param taxRate      税率，例如 0.1 表示 10%
     * @param discountCode 可选折扣码
     * @return PricingResult
     */
    public PricingResult calculateTotal(BigDecimal subtotal, BigDecimal taxRate, String discountCode) {
        if (subtotal == null) {
            throw new IllegalArgumentException("subtotal must not be null");
        }
        if (taxRate == null) {
            taxRate = BigDecimal.ZERO;
        }

        var discountResult = discountService.applyDiscount(subtotal, discountCode);
        BigDecimal discountAmount = discountResult.valid() ? discountResult.discountAmount() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        if (taxableAmount.compareTo(BigDecimal.ZERO) < 0) {
            taxableAmount = BigDecimal.ZERO;
        }

        BigDecimal taxAmount = taxableAmount.multiply(taxRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalAmount = taxableAmount.add(taxAmount);

        return new PricingResult(subtotal, discountAmount, taxableAmount, taxAmount, totalAmount);
    }
}

