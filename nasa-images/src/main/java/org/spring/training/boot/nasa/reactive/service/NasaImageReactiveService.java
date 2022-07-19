package org.spring.training.boot.nasa.reactive.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.training.boot.nasa.config.NasaProperties;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasaImageReactiveService {

    private final NasaProperties nasaProperties;

    private record Image(byte[] byteContent) {
        private static Image fromBytes(byte[] bytes) {
            return new Image(bytes);
        }

        private int contentSize() {
            return byteContent.length;
        }
    }

    public Mono<byte[]> getLargestImage(String url) {
        log.info("Sending request to {}", url);

        return WebClient.create(url)
                .get()
                .exchangeToMono(resp -> resp.bodyToMono(JsonNode.class))
                .flatMapIterable(jsonNode -> jsonNode.findValues(nasaProperties.imageTag()))
                .map(JsonNode::asText)
                .flatMap(this::buildImage)
                .log()
                .reduce((img1, img2) -> img1.contentSize() > img2.contentSize() ? img1 : img2)
                .map(Image::byteContent);
    }


    private Mono<Image> buildImage(String url) {
        log.info("Creating reactive web client to {}", url);
        WebClient client = WebClient.builder()
                .clientConnector(withConnector())
                .exchangeStrategies(withExchangeStrategies())
                .baseUrl(url)
                .build();

        return client.get()
                .retrieve()
                .bodyToMono(byte[].class)
                .map(Image::fromBytes);
    }

    private ClientHttpConnector withConnector() {
        return new ReactorClientHttpConnector(HttpClient.create().followRedirect(true));
    }

    private ExchangeStrategies withExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 100_000))
                .build();
    }
}
