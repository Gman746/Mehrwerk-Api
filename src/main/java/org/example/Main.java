package org.example;

import org.example.Utils.ApiUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Main {

    private static final ApiUtils apiUtils = new ApiUtils();

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringApplication.run(Main.class, args);

    }
}