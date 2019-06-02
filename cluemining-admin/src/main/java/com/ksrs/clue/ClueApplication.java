package com.ksrs.clue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ksrs.clue.mapper")
@EnableScheduling
public class ClueApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClueApplication.class, args);
	}
}
