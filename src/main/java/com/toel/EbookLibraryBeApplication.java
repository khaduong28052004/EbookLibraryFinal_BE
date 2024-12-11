package com.toel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
@EnableScheduling
public class EbookLibraryBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbookLibraryBeApplication.class, args);
		// UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");
		// String res1 = jedis.set("bike:1", "Deimos");
		// System.out.println(res1); // OK
		// String res2 = jedis.get("bike:1");
		// System.out.println(res2); // Deimos

	}

}
