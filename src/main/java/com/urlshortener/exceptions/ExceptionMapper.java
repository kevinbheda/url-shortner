package com.urlshortener.exceptions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@ControllerAdvice
public class ExceptionMapper {

	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping ; 	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorMessages handleValidException(final HttpServletRequest request, final MethodArgumentNotValidException ex)
	{
		final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		final List<String> errors = new ArrayList<String>(fieldErrors.size());
		String error;
		for (final FieldError fieldError : fieldErrors)
		{
			error = fieldError.getDefaultMessage();
			errors.add(error);
		}
		final ErrorMessages errorMessage = new ErrorMessages(errors);
		log.error(ex.getLocalizedMessage(), ex);
		return errorMessage;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ResponseBody
	ErrorMessages handleValidException(final HttpServletRequest request, final HttpMediaTypeNotSupportedException ex)
	{
		final List<String> errors =  new ArrayList<String>();
		errors.add("media type not supported");
		final ErrorMessages errorMessage = new ErrorMessages(errors);
		log.error(ex.getLocalizedMessage(), ex);
		return errorMessage;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	ErrorMessages handleValidException(final HttpServletRequest request, final EntityNotFoundException ex)
	{
		final List<String> errors =  new ArrayList<String>();
		errors.add(ex.getLocalizedMessage());
		final ErrorMessages errorMessage = new ErrorMessages(errors);
		log.error(ex.getLocalizedMessage(), ex);
		return errorMessage;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ResponseBody
	ErrorMessages handleValidException(final HttpServletRequest request, final CannotCreateShortUrlException ex)
	{
		final List<String> errors =  new ArrayList<String>();
		errors.add(ex.getLocalizedMessage());
		final ErrorMessages errorMessage = new ErrorMessages(errors);
		log.error(ex.getLocalizedMessage(), ex);
		return errorMessage;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ResponseBody
	ErrorMessages handleValidException(final HttpServletRequest request, final Exception ex)
	{
		final List<String> errors =  new ArrayList<String>();
		errors.add("Sorry for the inconvenience, internal server error occured.");
		final ErrorMessages errorMessage = new ErrorMessages(errors);
		log.error(ex.getLocalizedMessage(), ex);
		return errorMessage;
	}
	
}

