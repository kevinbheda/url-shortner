package com.urlshortener.exceptions;

public class CannotCreateShortUrlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotCreateShortUrlException(final String msg) {
		super(msg);
	}
}
