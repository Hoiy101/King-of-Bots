package com.kob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UntitledAddlication {
    public static void main(String[] args) {
        SpringApplication.run(UntitledAddlication.class, args);
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("yes");
        }
    }
}