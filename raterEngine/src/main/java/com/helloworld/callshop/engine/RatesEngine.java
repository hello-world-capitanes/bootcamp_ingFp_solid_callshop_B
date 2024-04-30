package com.helloworld.callshop.engine;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;
import com.helloworld.callshop.rater.rate.RatesRepository;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.RateFactoriesContainer;
import com.helloworld.callshop.rater.rate.factory.RateFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RatesEngine {

    private Scanner consoleInput;

    private ParametersReader consoleParametersReader;

    private RateFactoriesContainer factoriesContainer;


    public static void main(String[] args) throws Exception{
        RatesEngine engine = new RatesEngine();
        engineProvisioning(engine);
        engine.run();

    }

    public static void engineProvisioning(RatesEngine engine) throws Exception{

        engine.consoleInput =  new Scanner(System.in);

        engine.consoleParametersReader = new ConsoleParametersReader();

        engine.factoriesContainer = new RateFactoriesContainer();

        XMLConfigReader xmlConfigReader = new XMLConfigReader();
        xmlConfigReader.readConfiguration("configuration.xml");
        engine.factoriesContainer.createFactories(xmlConfigReader);
    }

    private String selectRateFactory() {

        System.out.println("Seleccione una opcion o 'x' para terminar:");

        Map<String, RateFactory> factoryMap = factoriesContainer.getFactories();

        String selectedOption = "";

        while (!factoryMap.containsKey(selectedOption) && !selectedOption.equalsIgnoreCase("x")) {

            factoryMap.forEach((id, factory) ->
                System.out.println(id + " --> " + factory.getDescription())
            );

            selectedOption = consoleInput.nextLine();
        }

        return selectedOption;
    }

    private List<JsonObject> listJsonRates () {
        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader("rates.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonObject data = gson.fromJson(reader, JsonObject.class);
        JsonArray ratesArray = data.getAsJsonArray("tarifas");
        List<JsonObject> ratesList = new ArrayList<>();
        for (JsonElement element : ratesArray) {
            ratesList.add(element.getAsJsonObject());
        }

        return ratesList;


    }

    public void run() throws Exception{


        processJson();
        //runConsole();


        //TESTEAMOS LAS TARIFAS CREADAS. DE AQUÍ HACIA ABAJO, ES SIMPLEMENTE DEMOSTRACIÓN DE FUNCIONAMIENTO Y QUEDA FUERA
        testRates();

    }

    private void runConsole() {
        String selectedFactory = "";

        while (!(selectedFactory = selectRateFactory()).equalsIgnoreCase("x")) {
            RateFactory factory = factoriesContainer.getFactories().get(selectedFactory);
            try {
                Rate rate = factory.makeRate(consoleParametersReader);
                RatesRepository.INSTANCE.addRate(rate);
            } catch (Exception e) {
                System.out.println("Error al crear la tarifa: " + e.getMessage() + "\n");
            }

        }
    }
    private void processJson() {
        List<JsonObject> ratesList = listJsonRates();
        System.out.println("Tarifas cargadas del json.");
        for (JsonObject jsonRate : ratesList) {
            RateFactory factory = factoriesContainer.getFactories().get(jsonRate.get("tipo").getAsString());
            try {
                Rate rate = factory.makeRate(new JsonParametersReader(jsonRate.get("parametros").getAsJsonObject()));
                RatesRepository.INSTANCE.addRate(rate);
            } catch (Exception e) {
                System.out.println("Error al procesar el json: " + e.getMessage() + "\n");
            }
        }
    }

    private void testRates() {
        RateableCall callSpain = new RateableCall("34915465454", 120, LocalTime.now());

        System.out.println("Comprobando las tarifas creadas para " + callSpain);


        for (Rate rate : RatesRepository.INSTANCE.getAllRates().values()) {
            BigDecimal charge = rate.calculatePrice(callSpain);
            System.out.println(rate + "-->" + charge);
        }

        System.out.println();

        RateableCall callUk = new RateableCall("444567345674", 120, LocalTime.now());

        System.out.println("Comprobando las tarifas creadas para " + callUk);

        for (Rate rate : RatesRepository.INSTANCE.getAllRates().values()) {
            BigDecimal charge = rate.calculatePrice(callUk);
            System.out.println(rate + "-->" + charge);
        }
    }

}
