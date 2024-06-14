package com.linkShortener.exception;

import com.linkShortener.entity.UrlMapping;

public class AliasAlreadyExistsException extends RuntimeException {

    public static UrlMapping aliasAlreadyExists(String customAlias) {
        var urlMapping = new UrlMapping();
        urlMapping.setAlias(customAlias);
        urlMapping.setErroCode("001");
        urlMapping.setDescription("CUSTOM ALIAS ALREADY EXISTS");
        return urlMapping;
    }
}