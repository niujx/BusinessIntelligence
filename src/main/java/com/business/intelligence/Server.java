package com.business.intelligence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

/**
 * Created by yanshi on 2017/7/14.
 */
@EnableScheduling
@Slf4j
@SpringBootApplication
public class Server {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        Server.context = SpringApplication.run(Server.class, args);
    }

    @PreDestroy
    public void close() {
        Server.context.close();
    }
}
