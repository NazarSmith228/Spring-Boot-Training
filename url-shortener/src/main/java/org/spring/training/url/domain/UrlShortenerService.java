package org.spring.training.url.domain;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.spring.training.url.domain.exception.UrlNotFoundException;
import org.spring.training.url.repository.ShortenedUrlRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "original_urls")
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortenedUrlRepository urlRepository;

    @Transactional
    public String performUrlShortening(String url, String title) {
        String urlId = RandomStringUtils.random(7, true, true);

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(url);
        shortenedUrl.setUrlId(urlId);
        shortenedUrl.setTitle(title);

        ShortenedUrl saved = urlRepository.save(shortenedUrl);
        return saved.getUrlId();
    }

    @Cacheable(key = "#root.args[0]")
    public String getOriginalUrl(String urlId) {
        return urlRepository.findByUrlId(urlId)
                .map(ShortenedUrl::getOriginalUrl)
                .orElseThrow(() -> new UrlNotFoundException("Couldn't find original url by: " + urlId));
    }
}
