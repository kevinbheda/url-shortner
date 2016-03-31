package com.urlshortener.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ShortenUrlDao {
	
	public  void addUrlEntry(final String key,  final String longUrl);
	
	public  Optional<String> getUrlEntry(final String shortKey);
	
	public void clearAllUrlEntries();
}
