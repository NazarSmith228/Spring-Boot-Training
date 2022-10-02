package org.spring.training.url.repository;

import org.spring.training.url.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    Optional<ShortenedUrl> findByUrlId(String urlId);

}
