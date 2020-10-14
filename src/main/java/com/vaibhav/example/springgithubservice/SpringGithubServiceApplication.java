package com.vaibhav.example.springgithubservice;

import com.vaibhav.example.springgithubservice.model.User;
import com.vaibhav.example.springgithubservice.service.GitHubLookUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@SpringBootApplication
public class SpringGithubServiceApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringGithubServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringGithubServiceApplication.class, args);
    }

    @Autowired
    public GitHubLookUpService gitHubLookUpService;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAysncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<User> page1 = gitHubLookUpService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = gitHubLookUpService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = gitHubLookUpService.findUser("Spring-Projects");

        CompletableFuture.allOf(page1, page2, page3).join();

        LOGGER.info("Time elapsed: " + (System.currentTimeMillis() - start));
        LOGGER.info("page1: " + page1.get());
        LOGGER.info("page2: " + page2.get());
        LOGGER.info("page3: " + page3.get());



    }
}
