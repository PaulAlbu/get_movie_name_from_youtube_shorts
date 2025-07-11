package com.retrieve.movie.title.youtube_shorts.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieFinderController {

    private final ChatClient chatClient;

    private final String systemInstructions = """
            You are a highly specialized AI designed exclusively for identifying movie titles from text/dialogue.
            **RESPOND ONLY WITH ONE OF THE FOLLOWING EXACT FORMATS, AND NOTHING ELSE:**
            * **" [Movie Title]"**
            * **"Uncertain: [Movie Title 1], [Movie Title 2]"** (list all uncertain titles)
            * **"Movie not identified"**
            **DO NOT** include any conversational filler, explanations, or additional text. Your output must be concise and strictly adhere to one of the above formats.
            """;

    public MovieFinderController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/find-movie")
    public String findMovie(@RequestBody String message) {

        return chatClient.prompt()
                .user(message)
                .system(systemInstructions)
                .call()
                .content();
    }

}
