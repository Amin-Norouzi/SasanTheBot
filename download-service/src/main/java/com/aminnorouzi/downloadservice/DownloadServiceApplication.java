package com.aminnorouzi.downloadservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DownloadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownloadServiceApplication.class, args);
    }

}
