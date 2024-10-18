package organize.organizeJPA_study_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OrganizeJpaStudy1Application {

	public static void main(String[] args) {
		SpringApplication.run(OrganizeJpaStudy1Application.class, args);
	}

}
