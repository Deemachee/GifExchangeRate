package ru.dmitrykorelov.gifexchangerate.feighclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "${exchange.feign.config.name}", url = "${exchange.feign.config.url}")
public interface ExchangeRateClient {

    @GetMapping("/latest.json?app_id=${exchange.feign.config.id}&" +
            "base=${exchange.feign.config.base}&symbols={currency}")
    String currencyToday(@PathVariable("currency") String currency);

    @GetMapping("/historical/{date}.json?app_id=${exchange.feign.config.id}&" +
            "base=${exchange.feign.config.base}&symbols={currency}")
    String currencyYesterday(@PathVariable("date") String date,
            @PathVariable("currency") String currency);


}