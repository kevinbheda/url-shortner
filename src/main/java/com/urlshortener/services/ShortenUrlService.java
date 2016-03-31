package com.urlshortener.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.urlshortener.exceptions.CannotCreateShortUrlException;

@Service
public interface ShortenUrlService {

	public String getShortenedUrl(String url) throws CannotCreateShortUrlException;
	public Optional<String> getOriginalUrl(String urlKey);
}
