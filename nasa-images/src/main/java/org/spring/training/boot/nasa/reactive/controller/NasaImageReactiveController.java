package org.spring.training.boot.nasa.reactive.controller;

import lombok.RequiredArgsConstructor;
import org.spring.training.boot.nasa.config.NasaProperties;
import org.spring.training.boot.nasa.reactive.service.NasaImageReactiveService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive/images")
@RequiredArgsConstructor
public class NasaImageReactiveController {

    private final NasaProperties nasaProperties;

    private final NasaImageReactiveService reactiveService;

    @GetMapping(value = "/{sol}/largest", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> getLargestImagePerSol(@PathVariable int sol) {
        String requestUrl = constructRequestUrl(sol);
        return reactiveService.getLargestImage(requestUrl);
    }

    private String constructRequestUrl(int sol) {
        return UriComponentsBuilder.fromHttpUrl(nasaProperties.baseUrl())
                .queryParam(nasaProperties.querySol(), sol)
                .queryParam(nasaProperties.queryApiKey(), nasaProperties.apiKey())
                .build()
                .toString();
    }

}
