package covid.vaccination.covin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class CovinApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovinApplication.class, args);
	}

}
