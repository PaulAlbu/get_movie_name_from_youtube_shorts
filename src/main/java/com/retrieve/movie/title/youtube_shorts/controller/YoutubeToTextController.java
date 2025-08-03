package com.retrieve.movie.title.youtube_shorts.controller;

import com.retrieve.movie.title.youtube_shorts.service.CovertVideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoutubeToTextController {
    private final CovertVideoService covertVideoService;
    public  static final String languageCode = "en";

    public YoutubeToTextController(CovertVideoService covertVideoService) {
        this.covertVideoService = covertVideoService;
    }

    @GetMapping("/get-movie/{*youtubeUrl}")
    public ResponseEntity<String> convertVideo(@PathVariable String youtubeUrl) {
        if(youtubeUrl.startsWith("/")){
            youtubeUrl = youtubeUrl.substring(1);
        }
        String message = covertVideoService.convertVideoToText(youtubeUrl, languageCode);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
