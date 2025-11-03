package com.tp.benchmarkspringmvcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BenchmarkSpringMvcApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BenchmarkSpringMvcApiApplication.class, args);
        System.out.println("Spring Boot app started at http://0.0.0.0:8080/");
        System.out.println("Prometheus metrics available at http://0.0.0.0:9404/actuator/prometheus");
    }

}