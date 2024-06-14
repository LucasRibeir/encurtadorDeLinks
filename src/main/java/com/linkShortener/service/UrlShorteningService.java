package com.linkShortener.service;

import com.linkShortener.entity.UrlMapping;
import com.linkShortener.exception.AliasAlreadyExistsException;
import com.linkShortener.exception.UrlNotFoundException;
import com.linkShortener.interfaces.IUrlShortening;
import com.linkShortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShorteningService implements IUrlShortening {

    @Autowired
    UrlMappingRepository urlMappingRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UrlMapping shortenUrl(String url, String customAlias) {
        var start = LocalDateTime.now();
        var urlMapping = new UrlMapping();

        if (customAlias != null && urlMappingRepository.findByAlias(customAlias).isPresent()) {
            return AliasAlreadyExistsException.aliasAlreadyExists(customAlias);
        }
        var alias = customAlias != null ? customAlias : generatedAlias();
        urlMapping.setOriginalUrl(url);
        urlMapping.setAlias(alias);
        var shortenerUrl = "http://shortener/u/" + alias;
        urlMapping.setShortenerUrl(shortenerUrl);
        var result = urlMappingRepository.save(urlMapping);
        if (result != null)
            urlMapping.setDescription("Success");

        var end = LocalDateTime.now();
        var duration = Duration.between(start, end);
        urlMapping.setTime_taken(duration.toMillis());

        return urlMapping;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UrlMapping> getUrlByAlias(String alias) {
        var result = urlMappingRepository.findByAlias(alias);
        return result.isPresent()  ? result : UrlNotFoundException.urlNotFound();
    }

    private String generatedAlias() {
        var random = new Random();
        var characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var alias = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            alias.append(characters.charAt(random.nextInt(characters.length())));
        }
        return alias.toString();
    }
}