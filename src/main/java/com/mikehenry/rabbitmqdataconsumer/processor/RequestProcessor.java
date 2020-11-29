package com.mikehenry.rabbitmqdataconsumer.processor;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestProcessor {

    Logger logger = LoggerFactory.getLogger(RequestProcessor.class);

    /**
     * To send the request to the endpoint for processing
     * @param url endpoint processing request
     * @param params request parameters
     * @return response String
     */
    public String processRequest(String url, String params) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> httpEntity = new HttpEntity<>(params, httpHeaders);

            logger.info("Sending request to: " + url);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            logger.info("Response: " + responseEntity.getBody() + " | " +
                    "HTTP Status: " + responseEntity.getStatusCode());

            return responseEntity.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("HTTP Exception occurred: " + e.getMessage());
        } catch (Exception e) {
            logger.error("General exception occurred: " + e.getMessage());
        }

        return emptyJSON();
    }

    /**
     * As a utility function in case response is empty
     * @return empty JSON
     */
    private String emptyJSON() {
        return "{}";
    }
}
