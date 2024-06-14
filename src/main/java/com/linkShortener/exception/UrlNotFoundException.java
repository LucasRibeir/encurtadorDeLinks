package com.linkShortener.exception;

import com.linkShortener.entity.UrlMapping;

import java.util.Optional;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String alias) {
        super("Shortened URL for alias " + alias + " not found");
    }

    public static Optional<UrlMapping> urlNotFound() {
        var urlMapping = new UrlMapping();
        urlMapping.setErroCode("002");
        urlMapping.setDescription("CUSTOM ALIAS ALREADY EXISTS");
        return Optional.of(urlMapping);
    }
}