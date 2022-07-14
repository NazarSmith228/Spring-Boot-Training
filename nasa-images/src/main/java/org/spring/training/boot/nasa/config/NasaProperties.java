package org.spring.training.boot.nasa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "nasa")
@ConstructorBinding
public record NasaProperties(String baseUrl, String apiKey, String querySol, String queryApiKey, String imageTag) {

}
