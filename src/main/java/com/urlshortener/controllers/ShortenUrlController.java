package com.urlshortener.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortener.exceptions.CannotCreateShortUrlException;
import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.requests.ShortenUrlRequest;
import com.urlshortener.responses.ShortUrlResponse;
import com.urlshortener.services.ShortenUrlService;

@RestController
public class ShortenUrlController {
	
	@Autowired
	private ShortenUrlService shortenUrlService;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/shorten-url", method = RequestMethod.POST)
	public ShortUrlResponse shortenUrl(@Valid @RequestBody final ShortenUrlRequest urlShortenRequest) 
			throws CannotCreateShortUrlException {
		log.info("shorten url request recieved");
		final String shortenedUrl = shortenUrlService.getShortenedUrl(urlShortenRequest.getUrl());
		log.info("short url generated " + shortenedUrl);
		return new ShortUrlResponse(urlShortenRequest.getUrl(), shortenedUrl);
	}
	
	@RequestMapping(value= "/{key}", method = RequestMethod.GET)
	public void redirect(@PathVariable("key") final String urlKey, final HttpServletResponse response) 
			throws EntityNotFoundException  {
		log.info("translate short url "+  urlKey +" request recieved ");
		final Optional<String> originalUrl = shortenUrlService.getOriginalUrl(urlKey);
		if(originalUrl.isPresent())	{
			log.info("redirect user to " + originalUrl.get());
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("Location", originalUrl.get());
		}
		else {
			throw new EntityNotFoundException("Error: unable to find the site url to redirect to");
		}
		
	}
}
