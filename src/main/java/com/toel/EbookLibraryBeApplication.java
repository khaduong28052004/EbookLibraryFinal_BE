package com.toel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class EbookLibraryBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbookLibraryBeApplication.class, args);
	}

}
