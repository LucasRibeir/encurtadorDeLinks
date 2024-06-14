package com.linkShortener.interfaces;

import com.linkShortener.entity.UrlMapping;

import java.util.Optional;

public interface IUrlShortening {
    UrlMapping shortenUrl(String url, String customAlias);

    Optional<UrlMapping> getUrlByAlias(String alias);

}