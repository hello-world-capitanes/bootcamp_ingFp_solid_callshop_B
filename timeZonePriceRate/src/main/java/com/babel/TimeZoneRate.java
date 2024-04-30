package com.babel;

import com.helloworld.callshop.rate.percentualprice.PercentualPriceRate;
import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public class TimeZoneRate extends AbstractRate implements Rate {

    private final LocalTime firstSlotStart;
    private final LocalTime secondSlotStart;
    private final LocalTime thirdSlotStart;

    private final Rate firstRate;
    private final Rate secondRate;
    private final Rate thirdRate;


    public TimeZoneRate(String name, LocalTime firstSlotStart, LocalTime secondSlotStart,
                        LocalTime thirdSlotStart, Rate firstRate, Rate secondRate,
                        Rate thirdRate){
        super(name);
        this.firstSlotStart = firstSlotStart;
        this.secondSlotStart = secondSlotStart;
        this.thirdSlotStart = thirdSlotStart;
        this.firstRate = firstRate;
        this.secondRate = secondRate;
        this.thirdRate = thirdRate;
    }

    @Override
    public BigDecimal calculatePrice(RateableCall call) {

        BigDecimal cost = BigDecimal.valueOf(0);

        ZonedDateTime firstSlotDT;
        ZonedDateTime secondSlotDT;
        ZonedDateTime thirdSlotDT;

        firstSlotDT = ZonedDateTime.now().with(firstSlotStart);
        secondSlotDT = ZonedDateTime.now().with(secondSlotStart);
        thirdSlotDT = ZonedDateTime.now().with(thirdSlotStart);


        ZonedDateTime slotStartCall = ZonedDateTime.from(call.getCallStart());

        if (secondSlotDT.isBefore(firstSlotDT)) {
            slotStartCall.plusDays(1);
        }
        if (thirdSlotDT.isBefore(secondSlotDT)) {
            slotStartCall.plusDays(1);
        }
        Rate slotRate = null;
        if(slotStartCall.isAfter(firstSlotDT) && slotStartCall.isBefore(secondSlotDT)){
            slotRate =firstRate;
        }

        if(slotStartCall.isAfter(secondSlotDT) && slotStartCall.isBefore(thirdSlotDT)){
            slotRate =secondRate;
        }

        if(slotStartCall.isAfter(thirdSlotDT) && slotStartCall.isBefore(firstSlotDT)){
            slotRate =thirdRate;
        }
        if (slotRate!=null) {
            cost = BigDecimal.valueOf(call.getDuration()).multiply(
                    slotRate.calculatePrice(new RateableCall(call.getInternationalPhoneNumber(),
                            call.getDuration(),
                            call.getCallStart())));
        }

        return cost;
    }

    @Override
    public String toString() {
        return "TimeZoneRate{" +
                "firstSlotStart=" + firstSlotStart +
                ", secondSlotStart=" + secondSlotStart +
                ", thirdSlotStart=" + thirdSlotStart +
                ", firstRate=" + firstRate +
                ", secondRate=" + secondRate +
                ", thirdRate=" + thirdRate +
                '}';
    }
}
