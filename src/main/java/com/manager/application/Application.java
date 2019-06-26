package com.manager.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com.manager.entity")
public class Application {

    public static void main(String[] args) {
        ApplicationContext application = SpringApplication.run(Application.class, args);
    }

}
