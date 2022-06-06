package ru.dmitrykorelov.gifexchangerate.controller;

import org.springframework.web.bind.annotation.*;
import ru.dmitrykorelov.gifexchangerate.exceptionhandler.GifExchangeException;
import ru.dmitrykorelov.gifexchangerate.service.GifExchangeService;
import java.util.Map;

@RestController
@RequestMapping("/get")
public class GifExchangeController {

    private final GifExchangeService service;

    public GifExchangeController(GifExchangeService service) {
        this.service = service;
    }

    @GetMapping("/{cur}")
    public Map<String, String> getGif(@PathVariable("cur") String cur) throws GifExchangeException {
        return service.getGifWithCurrency(cur);
    }

}
