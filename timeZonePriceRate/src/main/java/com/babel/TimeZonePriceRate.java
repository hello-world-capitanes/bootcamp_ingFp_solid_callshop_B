package com.babel;

import com.helloworld.callshop.rate.percentualprice.cost.CostCalculator;
import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TimeZonePriceRate extends AbstractRate implements Rate {

    private CostCalculator costCalculator;

    private final int marginPercent;

    public TimeZonePriceRate(String name, int marginPercent) {
        super(name);
        this.marginPercent = marginPercent;
    }

    @Override
    public BigDecimal calculatePrice(RateableCall call) {
        BigDecimal cost = costCalculator.getCost(call);
        BigDecimal marginPerOne = BigDecimal.valueOf(marginPercent / 100d);
        return cost.multiply(marginPerOne.add(BigDecimal.ONE)).setScale(6, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "TimeZonePriceRate{" +
                "costCalculator=" + costCalculator +
                ", marginPercent=" + marginPercent +
                '}';
    }
}
