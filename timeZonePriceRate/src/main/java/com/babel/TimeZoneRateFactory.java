package com.babel;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.*;

import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class TimeZoneRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa horaria (margen sobre en coste de la llamada)";

    private static final String ID = "TIMEZONE";
    private static final String SLOT1_TIMEZONE_NAME = "time_zone_1";
    private static final String SLOT2_TIMEZONE_NAME = "time_zone_2";
    private static final String SLOT3_TIMEZONE_NAME = "time_zone_3";

    private static final String SLOT1_RATE_NAME = "rateSlot1";
    private static final String SLOT2_RATE_NAME = "rateSlot2";
    private static final String SLOT3_RATE_NAME = "rateSlot3";


    private final Predicate<Object> timeSlotParameterValidator = parameter -> {
        try {
            LocalTime time = LocalTime.parse(parameter.toString());
            return true;
        } catch (InvalidParameterValueException | NullPointerException e) {
            return false;
        }
    };


    final private Parameter timeZoneParam1 = new Parameter(SLOT1_TIMEZONE_NAME,
            "Inicio primera franja",  timeSlotParameterValidator);
    final private Parameter timeZoneParam2 = new Parameter(SLOT2_TIMEZONE_NAME,
            "Inicio segunda franja", timeSlotParameterValidator);
    final private Parameter timeZoneParam3 = new Parameter(SLOT3_TIMEZONE_NAME,
            "Inicio tercera franja", timeSlotParameterValidator);

    final private Parameter slotRateParam1 = new Parameter(SLOT1_RATE_NAME, "tarifa primera franja", getNameValidator());
    final private Parameter slotRateParam2 = new Parameter(SLOT2_RATE_NAME, "tarifa segunda franja", getNameValidator());
    final private Parameter slotRateParam3 = new Parameter(SLOT3_RATE_NAME, "tarifa tercera franja", getNameValidator());



//    private boolean isValidTimeSlotParameter(LocalTime value) {
//        return value != null;
//    }

//    private boolean isNameRepeated(String rateName) {
//        /*
//        * lista.stream().map(e-> e.getId()).collect(Collectors.toList()) = lista de integer
//		               (e-> new Clase2(e.getId())
//		               * lista.stream().filter(e->e.getId() > 2).collect(Collectors.toList())*/
//
//        
//
//
//        for (TimeZoneRate rate : allRates) {
//            if (rate == rateEdited) {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean isRateExist(Rate rateEdited) {
        for (Rate rate : RatesRepository.INSTANCE.getAllRates().values()) {
            if (rate == rateEdited) {
                return true;
            }
        }
        return  false;
    }



    @Override
    public Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException, RateBuilderException {
        List<Parameter> parameters = getBasicParameterList();
        parameters.addAll(Arrays.asList(timeZoneParam1, slotRateParam1));
        parameters.addAll(Arrays.asList(timeZoneParam2, slotRateParam2));
        parameters.addAll(Arrays.asList(timeZoneParam3, slotRateParam3));

        ParametersMapper parametersMapper = parametersReader.readParameters(parameters);

        Rate rate1 = (Rate) parametersMapper.getValue(SLOT1_RATE_NAME);
        Rate rate2 = (Rate) parametersMapper.getValue(SLOT2_RATE_NAME);
        Rate rate3 = (Rate) parametersMapper.getValue(SLOT3_RATE_NAME);

        if (!isRateExist(rate1) || !isRateExist(rate2) || !isRateExist(rate3)) {
            throw new InvalidParameterValueException("Tarifa no existente");
        }


        LocalTime timeSlot1 = (LocalTime) parametersMapper.getValue(SLOT1_TIMEZONE_NAME);
        LocalTime timeSlot2 = (LocalTime) parametersMapper.getValue(SLOT2_TIMEZONE_NAME);
        LocalTime timeSlot3 = (LocalTime) parametersMapper.getValue(SLOT3_TIMEZONE_NAME);

        long differenceHoursSlo1Slot2 = Math.abs(timeSlot1.until(timeSlot2, ChronoUnit.HOURS));
        long differenceHoursSlot2Slot3 = Math.abs(timeSlot2.until(timeSlot3, ChronoUnit.HOURS));
        long differenceHoursSlot3Slot1 = Math.abs(timeSlot3.until(timeSlot1, ChronoUnit.HOURS));


        if (differenceHoursSlo1Slot2 > 22 || differenceHoursSlo1Slot2 < 1) {
            throw new RateBuilderException("Debe durar mínimo 1 hora y dejar mínimo 2 horas para las siguientes franjas");
        }

        if (differenceHoursSlo1Slot2 + differenceHoursSlot2Slot3 > 23 || differenceHoursSlot2Slot3 < 1) {
            throw new RateBuilderException("Debe durar mínimo 1 hora y dejar mínimo 1 hora para la última franja");
        }

        if (differenceHoursSlo1Slot2 + differenceHoursSlot2Slot3 + differenceHoursSlot3Slot1 != 24
                || differenceHoursSlot3Slot1 < 1) {
            throw new RateBuilderException("Debe durar mínimo 1 hora y no exceder el número de horas totales");
        }


        Object name = parametersMapper.getValue(RATE_NAME_NAME);

        if (!getNameValidator().test(name)) {
            throw new InvalidParameterValueException("Nombre no válido");
        }

        return new TimeZoneRate((String)name, timeSlot1, timeSlot2, timeSlot3, rate1, rate2, rate3);
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getId() {
        return ID;
    }
}
