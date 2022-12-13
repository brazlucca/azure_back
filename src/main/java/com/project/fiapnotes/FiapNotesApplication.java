package com.project.fiapnotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class FiapNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiapNotesApplication.class, args);
	}

}
