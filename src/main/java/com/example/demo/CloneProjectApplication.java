package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
@ServletComponentScan
@SpringBootApplication
public class CloneProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloneProjectApplication.class, args);
	}

}
