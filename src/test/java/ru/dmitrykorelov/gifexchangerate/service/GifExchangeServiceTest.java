package ru.dmitrykorelov.gifexchangerate.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.dmitrykorelov.gifexchangerate.exceptionhandler.GifExchangeException;
import ru.dmitrykorelov.gifexchangerate.feighclient.ExchangeRateClient;
import ru.dmitrykorelov.gifexchangerate.feighclient.GifClient;
import java.math.BigDecimal;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GifExchangeServiceTest {

    @Autowired
    GifExchangeService service;

    @MockBean
    GifClient gifClient;

    @MockBean
    ExchangeRateClient exchangeRateClient;

    //проверяем работу метода по поиску нужного курса
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

    //проверяем работу метода по поиску гифки
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
        String  findUrl = service.findUrlFromGiphyJson(json);
        assertNotNull(findUrl);
        assertEquals(expUrl, findUrl);
    }

    @Test
    public void getGifTest() {



    }

}
