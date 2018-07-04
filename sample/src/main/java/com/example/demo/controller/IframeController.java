package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.ipc.netty.http.server.HttpServerResponse;

@RestController
@Slf4j
public class IframeController {

	@RequestMapping(value="/iframe", method= RequestMethod.GET)
	public String iframe (HttpServletRequest request, HttpServerResponse response) {
		String tokenUrl = "http://221.139.81.194:7000/?private_token=S2jN12oLxzsdpG685iqv";
		String url = "http://221.139.81.194:7000/";
//		String url = "https://google.com";
		HttpGet httpGet = new  HttpGet(tokenUrl);
		
		HttpResponse httpResponse = null;
		String doc = null;

		HttpClient httpClient = HttpClientBuilder.create().build();
		
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			ContentType contentType = ContentType.getOrDefault(httpEntity);
			Charset charset = contentType.getCharset();

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), charset));
			
			StringBuffer dom = new StringBuffer();
			String line = reader.readLine();

			while ( line != null ) {
				line = line.replaceAll("href=\"/", "href=\"" + url).replaceAll("content=\"/", "content=\"" + url);
				dom.append(line);
				
		        line = reader.readLine();
		    }
			
			doc = dom.toString();
			log.debug(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
}
