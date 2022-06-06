package ru.dmitrykorelov.gifexchangerate.feighclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "${gif.feign.config.name}", url = "${gif.feign.config.url}")
public interface GifClient {

    @GetMapping("/gifs/random?api_key=${gif.feign.config.key}&tag=rich&rating=g")
    String getRichGif();

    @GetMapping("/gifs/random?api_key=${gif.feign.config.key}&tag=broke&rating=g")
    String getBrokeGif();

    @GetMapping("/gifs/random?api_key=${gif.feign.config.key}&tag=unchanged&rating=g")
    String getStabilityGif();


}
