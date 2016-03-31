package com.urlshortener.responses;

public class ShortUrlResponse {

	private final String originalUrl;
	//TODO change name to short Url
	private final String shortUrl;
	public ShortUrlResponse() 
	{
		super();
		this.originalUrl = "";
		this.shortUrl = "";
	}
	public ShortUrlResponse(final String originalUrl, final String shortUrlkey) {
		super();
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrlkey;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((originalUrl == null) ? 0 : originalUrl.hashCode());
		result = prime * result
				+ ((shortUrl == null) ? 0 : shortUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ShortUrlResponse other = (ShortUrlResponse) obj;
		if (originalUrl == null) {
			if (other.originalUrl != null)
				return false;
		} else if (!originalUrl.equals(other.originalUrl))
			return false;
		if (shortUrl == null) {
			if (other.shortUrl != null)
				return false;
		} else if (!shortUrl.equals(other.shortUrl))
			return false;
		return true;
	}
	
}
