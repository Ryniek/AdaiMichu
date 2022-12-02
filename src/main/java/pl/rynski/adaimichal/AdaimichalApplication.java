package pl.rynski.adaimichal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdaimichalApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdaimichalApplication.class, args);
	}
}
