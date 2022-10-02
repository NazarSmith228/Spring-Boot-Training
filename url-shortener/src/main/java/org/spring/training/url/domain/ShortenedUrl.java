package org.spring.training.url.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "shortened_urls")
@Getter
@Setter
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(name = "url_id", unique = true, nullable = false)
    private String urlId;

    @Column(name = "original_url", unique = true, nullable = false, updatable = false)
    private String originalUrl;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShortenedUrl that = (ShortenedUrl) o;
        return urlId != null && Objects.equals(urlId, that.urlId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
