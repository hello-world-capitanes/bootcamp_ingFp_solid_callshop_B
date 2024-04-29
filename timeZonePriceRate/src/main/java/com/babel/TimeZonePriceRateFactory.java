package com.babel;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.factory.InvalidParameterValueException;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.RateBuilderException;
import com.helloworld.callshop.rater.rate.factory.RateFactory;

public class TimeZonePriceRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa horaria (margen sobre en coste de la llamada)";

    private static final String ID = "TMZ";
    private static final String PERCENT_NAME = "time_zone";

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
