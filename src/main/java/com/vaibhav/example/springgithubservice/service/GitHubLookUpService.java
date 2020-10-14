package com.vaibhav.example.springgithubservice.service;

import com.vaibhav.example.springgithubservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;


@Service
public class GitHubLookUpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubLookUpService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public GitHubLookUpService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async()
    public CompletableFuture<User> findUser(String userName) throws InterruptedException {
        LOGGER.info("Looking up " + userName + " n GitHub");
        String url = String.format("https://api.github.com/users/%s", userName);
        User userResults = restTemplate.getForObject(url, User.class);
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(userResults);
    }
}
