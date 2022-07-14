package org.spring.training.boot.nasa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.training.boot.nasa.service.NasaImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class NasaImageController {

    private final NasaImageService imageService;

    @GetMapping("/{sol}/largest")
    public ResponseEntity<URI> getLargestImagePerSol(@Autowired HttpServletRequest request,
                                                     @PathVariable int sol) {
        log.info("Received request - {}", request.getRequestURL());

        Optional<URI> imageUri = imageService.getLargestImage(sol);

        return imageUri.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.status(HttpStatus.FOUND).location(imageUri.get()).build();
    }
}
