package ru.dmitrykorelov.gifexchangerate.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.dmitrykorelov.gifexchangerate.exceptionhandler.GifExchangeException;
import ru.dmitrykorelov.gifexchangerate.feighclient.ExchangeRateClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GifExchangeServiceTest {

    @Autowired
    GifExchangeService service;

    @MockBean
    ExchangeRateClient exchangeRateClient;

    //сравниваем результат выполнения метода поиска курса валюты на текущую дату и ручного запроса на апи сайта
    @Test
    public void exchangeRateTodayTest() throws IOException {
        String url = "https://openexchangerates.org/api/latest.json?app_id=" +
                "5fdb649018c94e6ca8c36349f69a9da8&symbols=RUB";
        String text = getHttpRequest(url);
        Mockito.when(exchangeRateClient.currencyToday("RUB")).thenReturn(text);
    }

    //сравниваем результат выполнения метода поиска курса валюты на дату 2022-06-01 и ручного запроса на апи сайта
    @Test
    public void exchangeRateYesterdayTest() throws IOException {
        String initDate = "%04d-%02d-%02d";
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.set(2022, Calendar.JUNE, 1);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);
        String historical = String.format(initDate, year, month, day);
        System.out.println(historical);
        String url = "https://openexchangerates.org/api/historical/2022-06-01" +
                ".json?app_id=5fdb649018c94e6ca8c36349f69a9da8&symbols=RUB";
        String text = getHttpRequest(url);
        Mockito.when(exchangeRateClient.currencyYesterday(historical, "RUB")).thenReturn(text);
    }


    //метод для ручного запроса на апи сайта обмена валют
    public String getHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        int byteRead;
        while ((byteRead = buffer.read()) != -1) {
            builder.append((char) byteRead);
        }
        buffer.close();
        return builder.toString();
    }

    //проверяем работу метода по поиску нужного курса из Json в виде строки
    @Test
    public void findExchangeRateFromJsonTest() throws GifExchangeException {
        String json = "{\"license\":\"https://openexchangerates.org/license\",\"" +
                "timestamp\":1654455619,\"base\":\"USD\",\"rates\":{\"AED\":3.6731,\"" +
                "AFN\":89.162943,\"ALL\":112.223267,\"AMD\":447.269866,\"ANG\":1.805741,\"" +
                "AOA\":426.1758,\"ARS\":120.67334,\"AUD\":1.387465,\"AWG\":1.8005,\"" +
                "AZN\":1.7,\"BAM\":1.824519}}";
        Map<String, BigDecimal> exchangeRates = service.findExchangeRateFromJson(json);
        assertNotNull(exchangeRates);
        assertEquals(0, exchangeRates.get("AMD")
                .compareTo(BigDecimal.valueOf(447.269866)));
    }

    //проверяем работу метода по поиску url гифки из Json в виде строки
    @Test
    public void findUrlFromGiphyJsonTest() throws GifExchangeException {
        String json = "{\"data\":{\"type\":\"gif\",\"id\":\"5C3Zrs5xUg5fHV4Kcf\",\"url\":\"" +
                "https://giphy.com/gifs/japan-money-usa-5C3Zrs5xUg5fHV4Kcf\",\"username\":\"" +
                "konczakowski\",\"rating\":\"g\",\"content_url\":\"\",\"source_tld\":\"\",\"" +
                "images\":{\"original\":{\"frames\":\"60\",\"size\":\"9550197\",\"url\":\"" +
                "https://media3.giphy.com/media/5C3Zrs5xUg5fHV4Kcf/giphy.gif?cid=" +
                "36ae8e177833fd86a07cffb1b46d62fa3e21d6185c1c2f5d&rid=giphy.gif&ct=g\",\"" +
                "webp_size\":\"2232816\",\"width\":\"480\"}}}}";

        String expUrl = "https://media3.giphy.com/media/5C3Zrs5xUg5fHV4Kcf/giphy.gif?cid=" +
                "36ae8e177833fd86a07cffb1b46d62fa3e21d6185c1c2f5d&rid=giphy.gif&ct=g";
        String findUrl = service.findUrlFromGiphyJson(json);
        assertNotNull(findUrl);
        assertEquals(expUrl, findUrl);
    }
}

