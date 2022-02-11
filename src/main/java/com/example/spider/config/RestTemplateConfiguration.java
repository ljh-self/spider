package com.example.spider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RestTemplateConfiguration
 * @author ljh
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean("restTemplateUtf8")
    public RestTemplate restTemplate(){
        return getInstance(StandardCharsets.UTF_8);
    }

    public static RestTemplate getInstance(Charset charset) {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(charset);
                break;
            }
        }
        return restTemplate;
    }

}
