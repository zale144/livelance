package livelance.app;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileSystemUtils;

import livelance.app.web.controller.FileUploadController;

@SpringBootApplication
public class HireMeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(HireMeApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init() {
		return (args) -> {
            FileSystemUtils.deleteRecursively(new File(FileUploadController.ROOT));

            Files.createDirectory(Paths.get(FileUploadController.ROOT));
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
 