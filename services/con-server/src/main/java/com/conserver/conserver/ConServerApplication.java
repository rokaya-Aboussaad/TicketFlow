package com.conserver.conserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ConServerApplication.class, args);
    }

}
