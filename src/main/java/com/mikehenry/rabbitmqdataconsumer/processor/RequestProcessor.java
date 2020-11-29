package com.mikehenry.rabbitmqdataconsumer.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RequestProcessor {

    Logger logger = LoggerFactory.getLogger(RequestProcessor.class);

    /**
     * To send the request to the endpoint for processing
     * @param url endpoint processing request
     * @param params request parameters
     * @return response String
     */
    public String processRequest(String url, String params) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                String.class);

        logger.info("Response: " + responseEntity.getBody() + " | " +
                "HTTP Status: " + responseEntity.getStatusCode());

        return responseEntity.getBody();
    }
}
