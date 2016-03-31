package com.urlshortener.dao.impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.urlshortener.dao.ShortenUrlDao;

@Repository
public class ShortenUrlDaoImpl implements ShortenUrlDao {

	private static HashMap<String, String> urlMap = new HashMap<>();
	
	@Override
	public synchronized void addUrlEntry(final String key,  final String longUrl) {
		urlMap.put(key, longUrl);
	}
	
	@Override
	public  synchronized Optional<String> getUrlEntry(final String shortKey) {
		final String longUrl = urlMap.get(shortKey);
		return Optional.ofNullable(longUrl);
	}
	
	@Override
	public void clearAllUrlEntries() {
		urlMap.clear();
	}
}
