package io.github.daihaowxg.sample01_spring_session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
public class Sample01SpringSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sample01SpringSessionApplication.class, args);
	}

}
