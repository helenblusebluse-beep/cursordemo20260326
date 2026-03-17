package com.nbcb.pay.service;

import com.nbcb.pay.model.DiscountCode;
import com.nbcb.pay.model.DiscountType;
import com.nbcb.pay.repository.DiscountCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DiscountService {

    public record DiscountResult(
            boolean valid,
            String message,
            BigDecimal discountAmount,
            BigDecimal finalAmount
    ) {
    }

    private final DiscountCodeRepository discountCodeRepository;

    public DiscountService(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    @Transactional(readOnly = true)
    public DiscountResult applyDiscount(BigDecimal orderAmount, String discountCodeValue) {
        if (discountCodeValue == null || discountCodeValue.isBlank()) {
            return new DiscountResult(false, "折扣码为空", BigDecimal.ZERO, orderAmount);
        }

        DiscountCode discountCode = discountCodeRepository.findByCodeIgnoreCase(discountCodeValue.trim())
                .orElse(null);
        if (discountCode == null) {
            return new DiscountResult(false, "折扣码不存在", BigDecimal.ZERO, orderAmount);
        }

        if (Boolean.FALSE.equals(discountCode.getEnabled())) {
            return new DiscountResult(false, "折扣码未启用", BigDecimal.ZERO, orderAmount);
        }

        LocalDateTime now = LocalDateTime.now();
        if (discountCode.getExpiresAt() != null && now.isAfter(discountCode.getExpiresAt())) {
            return new DiscountResult(false, "折扣码已过期", BigDecimal.ZERO, orderAmount);
        }

        if (discountCode.getMaxUses() != null
                && discountCode.getUsedCount() != null
                && discountCode.getUsedCount() >= discountCode.getMaxUses()) {
            return new DiscountResult(false, "折扣码使用次数已达上限", BigDecimal.ZERO, orderAmount);
        }

        if (discountCode.getMinOrderAmount() != null
                && orderAmount.compareTo(discountCode.getMinOrderAmount()) < 0) {
            return new DiscountResult(false, "未达到使用该折扣码的最低订单金额", BigDecimal.ZERO, orderAmount);
        }

        BigDecimal discountAmount = calculateDiscountAmount(orderAmount, discountCode);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new DiscountResult(false, "折扣金额无效", BigDecimal.ZERO, orderAmount);
        }

        BigDecimal finalAmount = orderAmount.subtract(discountAmount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
            discountAmount = orderAmount;
        }

        return new DiscountResult(true, "折扣码有效", discountAmount, finalAmount);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal orderAmount, DiscountCode discountCode) {
        if (discountCode.getType() == DiscountType.PERCENTAGE) {
            // amount 代表百分比，如 10 表示 10%
            BigDecimal percentage = discountCode.getAmount().divide(BigDecimal.valueOf(100));
            return orderAmount.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        if (discountCode.getType() == DiscountType.FIXED_AMOUNT) {
            return discountCode.getAmount();
        }
        return BigDecimal.ZERO;
    }
}

