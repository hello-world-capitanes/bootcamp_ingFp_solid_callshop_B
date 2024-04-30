package com.helloworld.callshop.engine;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.helloworld.callshop.rater.rate.factory.Parameter;
import com.helloworld.callshop.rater.rate.factory.ParametersMapper;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;

import java.util.List;

public class JsonParametersReader implements ParametersReader {

    JsonObject rate;

    public JsonParametersReader (JsonObject rate) {
        this.rate = rate;
    }
    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {
        ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

        for (Parameter parameter: parameters) {
            String parameterName = parameter.getName();
            String parameterValue = rate.get(parameterName.equalsIgnoreCase("NAME") ? "name" : parameterName)
                    .getAsString();
            if (!parameter.getValidator().test(parameterValue)) {
                System.out.println("\nDatos no válidos para la tarifa " + rate.get("name").getAsString()
                        + " en el campo " +
                        parameterName);
            }
            paramsMapper.put(parameter.getName(), parameterValue);
        }


        return paramsMapper;
    }
}
