package com.urlshortener.requests;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;
import org.hibernate.validator.constraints.NotBlank;

public class ShortenUrlRequest {
	
	@NotNull(message ="url cannot be empty")
	@NotBlank(message = "url cannot be blank")
	private String url;
	final static UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

	
	@AssertTrue(message = "url is not valid")
	private boolean isValidUrl() {
		if(urlValidator.isValid(url))	{
			return true;
		}

		return false;
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}
}
