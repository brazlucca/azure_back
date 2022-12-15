package com.project.fiapnotes;

import com.project.fiapnotes.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableConfigurationProperties({FileStorageProperties.class})
public class FiapNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiapNotesApplication.class, args);
	}

}
