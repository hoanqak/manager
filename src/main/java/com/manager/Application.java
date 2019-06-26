package com.manager;

import com.manager.model.Status;
import com.manager.repository.RoleJpa;
import com.manager.repository.StatusRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com")
public class Application {
    public static void main(String[] args) {
        ApplicationContext application = SpringApplication.run(Application.class, args);
        RoleJpa statusRepository = application.getBean(RoleJpa.class);
        System.out.println(statusRepository);
    }

}
