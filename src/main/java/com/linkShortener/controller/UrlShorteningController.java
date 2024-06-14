package com.linkShortener.controller;

import com.linkShortener.entity.UrlMapping;
import com.linkShortener.interfaces.IUrlShortening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/shortener")
public class UrlShorteningController {

    @Autowired
    IUrlShortening shorteningService;

    @PutMapping("/create")
    public UrlMapping createShortenedUrl(@RequestParam("url") String url, @RequestParam(value = "custom-alias", required = false) String customAlias) {
        return shorteningService.shortenUrl(url, customAlias);
    }

    @GetMapping("/retrieve")
    public Optional<UrlMapping> retrieveOriginalUrl(@RequestParam("alias") String alias) {
        return shorteningService.getUrlByAlias(alias);
    }
}
