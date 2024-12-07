package com.toel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import vn.payos.PayOS;

@SpringBootApplication
@EnableScheduling
public class EbookLibraryBeApplication {


	public static void main(String[] args) {
		SpringApplication.run(EbookLibraryBeApplication.class, args);
	}

}
