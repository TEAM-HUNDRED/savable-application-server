package net.app.savable;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.util.ArrayList;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 30)
@EnableCaching
public class SavableApplication {

	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		try {
			for(int i=0; i < 250000; i++) {
				list.add(new int[10000000]); // 리스트에 배열을 추가한다
				System.out.println(i);
				Thread.sleep(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SpringApplication.run(SavableApplication.class, args);
		}

	@PostConstruct
	void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
