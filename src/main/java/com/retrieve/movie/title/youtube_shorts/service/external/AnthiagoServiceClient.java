package com.retrieve.movie.title.youtube_shorts.service.external;

import com.retrieve.movie.title.youtube_shorts.exceptions.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AnthiagoServiceClient {
    private final RestTemplate restTemplate;
    private final String apiUrl = "https://apiv2.anthiago.com/transcript?get_video=%s&codeL=%s&status=false";
    private final String ORIGIN_HEADER_NAME = "Origin";
    private final String ORIGIN_HEADER_VALUE = "https://anthiago.com";
    private final String REFERER_HEADER_NAME = "Referer";
    private final String REFERER_HEADER_VALUE = "https://anthiago.com/desgrabador/";
    private final Logger logger = LoggerFactory.getLogger(AnthiagoServiceClient.class);


    public AnthiagoServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String requestTextFromVideo(String youtubeUrl, String languageCode) {
        String url = String.format(apiUrl, youtubeUrl, languageCode);


        HttpHeaders headers = addHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            if (response.getBody() == null) {
                logger.warn("Anthiago service returned null body for URL: {}", url);
                throw new ExternalServiceException("Anthiago service returned empty content for video: " + youtubeUrl);
            }
            return response.getBody();
        } catch (Exception e){
            logger.error("Error during API call to Anthiago for URL: {}. Message: {}", url, e.getMessage());
            throw new ExternalServiceException("Failed to retrieve text from Anthiago service for video: " + youtubeUrl, e);
        }
    }

    private HttpHeaders addHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ORIGIN_HEADER_NAME, ORIGIN_HEADER_VALUE);
        headers.set(REFERER_HEADER_NAME, REFERER_HEADER_VALUE);
        return headers;
    }


}
