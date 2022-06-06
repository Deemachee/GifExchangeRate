package ru.dmitrykorelov.gifexchangerate.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import ru.dmitrykorelov.gifexchangerate.exceptionhandler.GifExchangeException;
import ru.dmitrykorelov.gifexchangerate.feighclient.ExchangeRateClient;
import ru.dmitrykorelov.gifexchangerate.feighclient.GifClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@Service
public class GifExchangeService {

    private final ExchangeRateClient exchangeRateClient;

    private final GifClient gifClient;

    public GifExchangeService(ExchangeRateClient exchangeRateClient, GifClient gifClient) {
        this.exchangeRateClient = exchangeRateClient;
        this.gifClient = gifClient;
    }

    //получаем ответ от сервиса валют и преобразовываем в json, где находим объект rates
    //заносим в мапу ключи и значения
    public Map<String, BigDecimal> findExchangeRateFromJson(String jsonString) throws GifExchangeException {
        try {
            Map<String, BigDecimal> exchangeRates = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonString);
            Iterator<Map.Entry<String, JsonNode>> fieldNames = node.get("rates").getFields();
            fieldNames.forEachRemaining(e -> exchangeRates.put(e.getKey(), e.getValue().getDecimalValue()));
            return exchangeRates;
        } catch (IOException e) {
            throw new GifExchangeException("Some problems with https://openexchangerates.org/api");
        } catch (NullPointerException e) {
            throw new GifExchangeException("You wrote wrong currency!");
        }
    }

    //получаем текущий курс валюты из аргумента
    public BigDecimal exchangeRateToday(String currency) throws GifExchangeException {
        String rateToday = exchangeRateClient.currencyToday(currency);
        return findExchangeRateFromJson(rateToday).get(currency);
    }

    //получаем вчерашний курс валюты из аргумента
    public BigDecimal exchangeRateYesterday(String currency) throws GifExchangeException {
        String initDate = "%04d-%02d-%02d";
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.add(Calendar.DAY_OF_MONTH, -1);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);
        String historical = String.format(initDate, year, month, day);
        String rateYesterday = exchangeRateClient.currencyYesterday(historical, currency);
        return findExchangeRateFromJson(rateYesterday).get(currency);
    }

    //получаем ответ от сервиса гифок после запроса, ищем объект original и поле url искомой gif
    public String findUrlFromGiphyJson(String jsonString) throws GifExchangeException {
        try {
            JSONObject json = new JSONObject(jsonString);
            return json.getJSONObject("data").getJSONObject("images")
                    .getJSONObject("original").getString("url");
        } catch (JSONException | NullPointerException e) {
            throw new GifExchangeException("Some problems with JSON at https://api.giphy.com/v1");
        }
    }

    //сравниваем курс валюты по отношению к вчерашнему, и присылаем в ответ ссылку на гифку
    public String getGifWithCurrency(String currency) throws GifExchangeException {
        BigDecimal today = exchangeRateToday(currency);
        BigDecimal yesterday = exchangeRateYesterday(currency);
        int dif = today.compareTo(yesterday);
        String jsonString;
        if (dif > 0) {
            jsonString = gifClient.getBrokeGif();
        } else if (dif < 0) {
            jsonString = gifClient.getRichGif();
        } else {
            jsonString = gifClient.getStabilityGif();
        }
        return findUrlFromGiphyJson(jsonString);
    }

}
