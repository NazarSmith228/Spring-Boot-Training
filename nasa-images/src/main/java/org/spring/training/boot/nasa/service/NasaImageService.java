package org.spring.training.boot.nasa.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.training.boot.nasa.config.NasaProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "largestImage", cacheManager = "ehCacheManager")
@RequiredArgsConstructor
@Slf4j
public class NasaImageService {

    private final RestTemplate restTemplate;

    private final NasaProperties nasaProperties;

    @CacheEvict(allEntries = true, condition = "@environment.getProperty('nasa.annotation-cache')")
    @Scheduled(cron = "0 0 12 * * *")
    public void clearCache() {
        log.info("Clearing NASA largest images cache...");
    }

    @Cacheable
    public Optional<URI> getLargestImage(int sol) {
        URI uri = buildUri(sol);
        log.info("Sending request to base NASA url - {}", uri);

        return Optional.ofNullable(restTemplate.getForObject(uri, JsonNode.class))
                .stream()
                .flatMap(jsonResponse -> jsonResponse.findValues(nasaProperties.imageTag())
                        .stream()
                        .map(node -> URI.create(node.asText())))
                .parallel()
                .map(this::createImageSizePair)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    private URI buildUri(int sol) {
        return UriComponentsBuilder.fromHttpUrl(nasaProperties.baseUrl())
                .queryParam(nasaProperties.querySol(), sol)
                .queryParam(nasaProperties.queryApiKey(), nasaProperties.apiKey())
                .build()
                .toUri();
    }

    private Map.Entry<URI, Long> createImageSizePair(URI uri) {
        log.info("Sending request for image size - {}", uri);

        HttpHeaders responseHeaders = restTemplate.headForHeaders(uri);
        URI redirectLocation = responseHeaders.getLocation();

        while (Objects.nonNull(redirectLocation)) {
            log.info("Sending redirect request {}", redirectLocation);

            responseHeaders = restTemplate.headForHeaders(redirectLocation);
            uri = redirectLocation;
            redirectLocation = responseHeaders.getLocation();
        }

        return Map.entry(uri, responseHeaders.getContentLength());
    }

}
