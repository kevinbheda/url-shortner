package com.urlshortener.exceptions;

import java.util.List;

public class ErrorMessages {

	private final List<String> errors;
	public ErrorMessages(final List<String> errors) {
		this.errors = errors;
	}
	public List<String> getErrors() {
		return errors;
	}

}
