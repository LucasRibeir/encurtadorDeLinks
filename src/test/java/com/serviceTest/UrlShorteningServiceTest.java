package com.serviceTest;

import com.linkShortener.entity.UrlMapping;
import com.linkShortener.exception.AliasAlreadyExistsException;
import com.linkShortener.exception.UrlNotFoundException;
import com.linkShortener.repository.UrlMappingRepository;
import com.linkShortener.service.UrlShorteningService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class UrlShorteningServiceTest {

    @Mock
    UrlMappingRepository urlMappingRepository;

    @InjectMocks
    UrlShorteningService urlShorteningService;

    private String url;
    private String customAlias;
    private String alias;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        url = "www.bemobi.com.br";
        customAlias = "bemobi";
        alias = "bemobi";
    }

    @Test
    void test_shorten_url_alias_already_exists() {
        var existingMapping = new UrlMapping();
        existingMapping.setAlias(customAlias);

       Mockito.when(urlMappingRepository.findByAlias(customAlias)).thenReturn(Optional.of(existingMapping));

        var result = urlShorteningService.shortenUrl(url, customAlias);

        Assertions.assertEquals("001", result.getErroCode());
        Assertions.assertEquals("CUSTOM ALIAS ALREADY EXISTS", result.getDescription());
        Assertions.assertEquals(customAlias, result.getAlias());
    }

    @Test
    void test_shorten_url_with_custom_alias() {
        Mockito.when(urlMappingRepository.findByAlias(customAlias)).thenReturn(Optional.empty());

        var urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(url);
        urlMapping.setAlias(customAlias);
        Mockito.when(urlMappingRepository.save(any(UrlMapping.class))).thenReturn(urlMapping);

        var result = urlShorteningService.shortenUrl(url, customAlias);

        Assertions.assertEquals(customAlias, result.getAlias());
        Assertions.assertEquals(url, result.getOriginalUrl());


    }

    @Test
    void test_retrieve_original_url() {
        var urlMapping = new UrlMapping();
        urlMapping.setAlias(alias);
        var optionalUrlMapping = Optional.of(urlMapping);

        Mockito.when(urlMappingRepository.findByAlias(alias)).thenReturn(optionalUrlMapping);

        var result = urlShorteningService.getUrlByAlias(alias);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(urlMapping, result.get());
    }

    @Test
    void test_retrieve_original_url_notFound() {
        Mockito.when(urlMappingRepository.findByAlias(alias)).thenReturn(Optional.empty());

        var result = urlShorteningService.getUrlByAlias(alias);

        Assertions.assertTrue(result.isPresent());
        var errorUrlMapping = result.get();
        Assertions.assertEquals("002", errorUrlMapping.getErroCode());
        Assertions.assertEquals("CUSTOM ALIAS ALREADY EXISTS", errorUrlMapping.getDescription());
    }
    }

