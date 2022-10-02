package org.spring.training.url.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.training.url.domain.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1/url/short")
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerController {

    private final UrlShortenerService shortenerService;

    @PostMapping
    public ResponseEntity<Void> shortenUrl(@RequestBody ShortenUrlRequest request) {
        log.debug("Url shortening request: {}", request);
        String url = request.url();
        String title = request.title();

        String shortenedUrl = shortenerService.performUrlShortening(url, title);
        URI shortenedUri = buildShortenedUri(shortenedUrl);

        return ResponseEntity
                .created(shortenedUri)
                .build();
    }

    @GetMapping("/{urlId}")
    public ResponseEntity<Void> redirectByShortenedUrl(@PathVariable("urlId") String shortenedUrl) {
        log.debug("Redirect request for shortened url: {}", shortenedUrl);
        String originalUrl = shortenerService.getOriginalUrl(shortenedUrl);

        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(originalUrl))
                .build();
    }

    private URI buildShortenedUri(String shortenedUrl) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{urlId}")
                .buildAndExpand(shortenedUrl)
                .toUri();
    }

    private record ShortenUrlRequest(String url, String title) {
    }
}
