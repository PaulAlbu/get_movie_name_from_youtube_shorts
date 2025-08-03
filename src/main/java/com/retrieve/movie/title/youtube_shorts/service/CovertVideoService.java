package com.retrieve.movie.title.youtube_shorts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retrieve.movie.title.youtube_shorts.service.external.AnthiagoServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CovertVideoService {

    private final AnthiagoServiceClient anthiagoServiceClient;
    private final Logger logger = LoggerFactory.getLogger(CovertVideoService.class);
    private final MovieFinderService movieFinderService;

    public CovertVideoService(AnthiagoServiceClient anthiagoServiceClient, MovieFinderService movieFinderService) {

        this.anthiagoServiceClient = anthiagoServiceClient;
        this.movieFinderService = movieFinderService;
    }

    public String convertVideoToText(String youtubeUrl, String languageCode) {
        String movieTextRaw = anthiagoServiceClient.requestTextFromVideo(youtubeUrl, languageCode);
        String filteredText = filterMovieText(movieTextRaw);
        return movieFinderService.findMovie(filteredText);
    }

    private String filterMovieText(String movieTextRaw) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(movieTextRaw);
            JsonNode subtitlesNode = jsonNode.get("subtitles");
            StringBuilder stringBuilder = new StringBuilder();
            for (JsonNode entry : subtitlesNode) {
                if (entry.has("f")) {
                    if (!stringBuilder.isEmpty()) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(entry.get("f").asText());
                }
            }
            return stringBuilder.toString().trim();
        } catch (JsonProcessingException e) {
            logger.error("Error while cleaning converted text from video: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
