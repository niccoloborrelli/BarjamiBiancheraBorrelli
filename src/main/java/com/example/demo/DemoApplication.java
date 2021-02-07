package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class DemoApplication {

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		System.out.println("Server started");
		System.out.println(TimeZone.getDefault());
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
