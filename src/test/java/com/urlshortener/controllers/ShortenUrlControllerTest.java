package com.urlshortener.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.Application;
import com.urlshortener.dao.ShortenUrlDao;
import com.urlshortener.requests.ShortenUrlRequest;
import com.urlshortener.responses.ShortUrlResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ShortenUrlControllerTest {

	@Autowired
	protected WebApplicationContext wac;
	
	@Autowired
	protected ShortenUrlDao shortenUrlDao; 

	protected MockMvc mockMvc;
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@Autowired
    void setConverters(final HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
	}
	
	@After
	public void tearDown() {
		shortenUrlDao.clearAllUrlEntries();
	}
	
	
	@Test
	public void testShortenUrl() throws Exception {
		final ShortenUrlRequest request = new ShortenUrlRequest();
		request.setUrl("http://www.google.com");
		final String requestJson = this.json(request);
		final MvcResult mvcResult = mockMvc.perform(post("/shorten-url")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		final ObjectMapper mapper = new ObjectMapper();
		final ShortUrlResponse shortUrlResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), ShortUrlResponse.class);
		assertThat(request.getUrl(), equalTo(shortUrlResponse.getOriginalUrl()));
		assertNotNull(shortUrlResponse.getShortUrl());
		
		//assert that subsequent request with the same url does not fail
		final MvcResult mvcResult2 = mockMvc.perform(post("/shorten-url")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		final ShortUrlResponse shortUrlResponse2 = mapper.readValue(mvcResult2.getResponse().getContentAsString(), ShortUrlResponse.class);
		assertThat(shortUrlResponse, equalTo(shortUrlResponse2));

		//assert that generated short url works
		mockMvc.perform(get(shortUrlResponse.getShortUrl()))
		.andDo(print())
		.andExpect(redirectedUrl(shortUrlResponse.getOriginalUrl()))
		.andReturn();
	}
	
	@Test
	public void testInvalidShortUrl() throws Exception {
		final String invalidShortUrl = "/sdsdsdsdsdsdsdsdsds";
		mockMvc.perform(get(invalidShortUrl))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.errors[*]", hasSize(1)))
		.andExpect(jsonPath("$.errors[0]", is("Error: unable to find the site url to redirect to")))
		.andReturn();
	}
	
	@Test
	public void testInvalidUrlToShortenRequest() throws Exception {
		
		final String invalidUrl = "sss.com.s";
		final ShortenUrlRequest request = new ShortenUrlRequest();
		request.setUrl(invalidUrl);
		final String requestJson = this.json(request);
		
		mockMvc.perform(post("/shorten-url")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*]", hasSize(1)))
				.andExpect(jsonPath("$.errors[0]", is("url is not valid")))
				.andReturn();
		
	}
	
	@Test
	public void testInvalidBlankUrlToShorten() throws Exception {
		final String blankUrl = "   ";
		final ShortenUrlRequest request = new ShortenUrlRequest();
		request.setUrl(blankUrl);
		final String requestJson = this.json(request);
		
		mockMvc.perform(post("/shorten-url")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*]", hasSize(2)))
				.andExpect(jsonPath("$.errors[*]", 
						containsInAnyOrder("url is not valid", "url cannot be blank")))
				.andReturn();

	}
	
	@Test
	public void testInvalidEmptyUrlToShorten() throws Exception {
		final String blankUrl = null;
		final ShortenUrlRequest request = new ShortenUrlRequest();
		request.setUrl(blankUrl);
		final String requestJson = this.json(request);
		
		mockMvc.perform(post("/shorten-url")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*]", hasSize(3)))
				.andExpect(jsonPath("$.errors[*]", 
						containsInAnyOrder("url is not valid", "url cannot be blank", "url cannot be empty")))
				.andReturn();

	}
	
	public String json(final Object o) throws IOException {
        final MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        
        return mockHttpOutputMessage.getBodyAsString();
    }
}
