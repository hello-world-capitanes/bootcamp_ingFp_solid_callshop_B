package com.babel;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.factory.InvalidParameterValueException;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.RateBuilderException;
import com.helloworld.callshop.rater.rate.factory.RateFactory;

import java.beans.XMLDecoder;

public class TimeZonePriceRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa horaria (margen sobre en coste de la llamada)";

    private static final String ID = "TIMEZONE";
    private static final String SLOT1 = "time_zone_morning";
    private static final String SLOT2 = "time_zone_afternoon";
    private static final String SLOT3 = "time_zone_night";

    private static final String SLOT1_RATE_NAME = "rateSlot1";
    private static final String SLOT2_RATE_NAME = "rateSlot2";
    private static final String SLOT3_RATE_NAME = "rateSlot3";


    //private final CostCalculator costCalculator = new SimpleCostCalculator();
    @Override
    public Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException, RateBuilderException {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
