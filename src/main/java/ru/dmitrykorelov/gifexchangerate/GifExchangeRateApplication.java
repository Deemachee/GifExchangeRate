package ru.dmitrykorelov.gifexchangerate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.dmitrykorelov.gifexchangerate.service.GifExchangeService;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
public class GifExchangeRateApplication {

    public static void main(String[] args) {

        SpringApplication.run(GifExchangeRateApplication.class, args);



    }
}
