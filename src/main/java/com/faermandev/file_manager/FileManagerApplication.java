package com.faermandev.file_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class FileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileManagerApplication.class, args);
	}

}
