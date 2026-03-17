package com.nbcb.pay.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PricingServiceTest {

    @Test
    void shouldApplyDiscountBeforeTax() {
        DiscountService discountService = mock(DiscountService.class);
        PricingService pricingService = new PricingService(discountService);

        when(discountService.applyDiscount(BigDecimal.valueOf(200), "CODE"))
                .thenReturn(new DiscountService.DiscountResult(
                        true,
                        "ok",
                        BigDecimal.valueOf(20),
                        BigDecimal.valueOf(180)
                ));

        var result = pricingService.calculateTotal(BigDecimal.valueOf(200), BigDecimal.valueOf(0.1), "CODE");

        assertEquals(BigDecimal.valueOf(200), result.subtotal());
        assertEquals(BigDecimal.valueOf(20), result.discountAmount());
        assertEquals(BigDecimal.valueOf(180), result.taxableAmount());
        assertEquals(BigDecimal.valueOf(18.00).setScale(2), result.taxAmount());
        assertEquals(BigDecimal.valueOf(198.00).setScale(2), result.totalAmount());
    }
}

