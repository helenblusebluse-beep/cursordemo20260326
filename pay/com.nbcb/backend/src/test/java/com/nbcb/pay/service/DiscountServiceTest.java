package com.nbcb.pay.service;

import com.nbcb.pay.model.DiscountCode;
import com.nbcb.pay.model.DiscountType;
import com.nbcb.pay.repository.DiscountCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiscountServiceTest {

    private DiscountCodeRepository repository;
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        repository = mock(DiscountCodeRepository.class);
        discountService = new DiscountService(repository);
    }

    @Test
    void shouldReturnInvalidWhenCodeNotFound() {
        when(repository.findByCodeIgnoreCase("ABC")).thenReturn(Optional.empty());

        var result = discountService.applyDiscount(BigDecimal.valueOf(100), "ABC");

        assertFalse(result.valid());
        assertEquals(BigDecimal.ZERO, result.discountAmount());
        assertEquals(BigDecimal.valueOf(100), result.finalAmount());
    }

    @Test
    void shouldApplyPercentageDiscount() {
        DiscountCode code = new DiscountCode();
        code.setCode("P10");
        code.setType(DiscountType.PERCENTAGE);
        code.setAmount(BigDecimal.TEN); // 10%
        code.setEnabled(true);
        when(repository.findByCodeIgnoreCase("P10")).thenReturn(Optional.of(code));

        var result = discountService.applyDiscount(BigDecimal.valueOf(200), "P10");

        assertTrue(result.valid());
        assertEquals(BigDecimal.valueOf(20.00).setScale(2), result.discountAmount());
        assertEquals(BigDecimal.valueOf(180.00).setScale(2), result.finalAmount());
    }

    @Test
    void shouldApplyFixedAmountDiscountWithFloorAtZero() {
        DiscountCode code = new DiscountCode();
        code.setCode("F300");
        code.setType(DiscountType.FIXED_AMOUNT);
        code.setAmount(BigDecimal.valueOf(300));
        code.setEnabled(true);
        when(repository.findByCodeIgnoreCase("F300")).thenReturn(Optional.of(code));

        var result = discountService.applyDiscount(BigDecimal.valueOf(100), "F300");

        assertTrue(result.valid());
        assertEquals(BigDecimal.valueOf(100), result.discountAmount());
        assertEquals(BigDecimal.ZERO, result.finalAmount());
    }

    @Test
    void shouldRejectExpiredCode() {
        DiscountCode code = new DiscountCode();
        code.setCode("EXP");
        code.setType(DiscountType.FIXED_AMOUNT);
        code.setAmount(BigDecimal.TEN);
        code.setEnabled(true);
        code.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(repository.findByCodeIgnoreCase("EXP")).thenReturn(Optional.of(code));

        var result = discountService.applyDiscount(BigDecimal.valueOf(100), "EXP");

        assertFalse(result.valid());
    }

    @Test
    void shouldRejectWhenBelowMinOrderAmount() {
        DiscountCode code = new DiscountCode();
        code.setCode("MIN100");
        code.setType(DiscountType.FIXED_AMOUNT);
        code.setAmount(BigDecimal.TEN);
        code.setEnabled(true);
        code.setMinOrderAmount(BigDecimal.valueOf(100));
        when(repository.findByCodeIgnoreCase("MIN100")).thenReturn(Optional.of(code));

        var result = discountService.applyDiscount(BigDecimal.valueOf(50), "MIN100");

        assertFalse(result.valid());
    }
}

