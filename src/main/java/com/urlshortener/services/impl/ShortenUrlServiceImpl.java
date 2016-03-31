package com.urlshortener.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.urlshortener.dao.ShortenUrlDao;
import com.urlshortener.exceptions.CannotCreateShortUrlException;
import com.urlshortener.services.ShortenUrlService;

@Service
public class ShortenUrlServiceImpl implements ShortenUrlService {

	@Autowired
	private ShortenUrlDao shortenUrlDao;
	private static final String baseUrl = "http://localhost:8080/";
	private final Logger log = LoggerFactory.getLogger(this.getClass());	
	
	@Override
	public String getShortenedUrl(final String url) throws CannotCreateShortUrlException {
		final String urlKey = generateUrlKey(url);
		final Optional<String> urlEntry = shortenUrlDao.getUrlEntry(urlKey);
		if(urlEntry.isPresent() &&(! url.equals(urlEntry.get())))
		{
			throw new CannotCreateShortUrlException("Could not generate unique short url ");
		}
		else {
			//add
			shortenUrlDao.addUrlEntry(urlKey, url);
		}
		return baseUrl + urlKey;
	}
	@Override
	public Optional<String> getOriginalUrl(final String urlKey) {
		
		final Optional<String> urlEntry = shortenUrlDao.getUrlEntry(urlKey);
		return urlEntry;
	}

	private String generateUrlKey(final String url) {
		final String urlKey = Hashing.murmur3_32().
				hashString(url, StandardCharsets.UTF_8)
				.toString();
		return urlKey;
	}
	
	
}
