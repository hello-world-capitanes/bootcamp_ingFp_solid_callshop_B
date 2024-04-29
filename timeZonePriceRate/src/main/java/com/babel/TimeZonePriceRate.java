package com.babel;

import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;

public class TimeZonePriceRate extends AbstractRate implements Rate {
    public TimeZonePriceRate(String name) {
        super(name);
    }

    @Override
    public BigDecimal calculatePrice(RateableCall call) {
        return null;
    }
}
