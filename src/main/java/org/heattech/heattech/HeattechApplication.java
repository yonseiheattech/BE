package org.heattech.heattech;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeattechApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("KAKAO_CLIENT_ID", dotenv.get("KAKAO_CLIENT_ID"));
		System.setProperty("KAKAO_REDIRECT_URI", dotenv.get("KAKAO_REDIRECT_URI"));
		System.setProperty("KAKAO_TOKEN_URI", dotenv.get("KAKAO_TOKEN_URI"));
		System.setProperty("KAKAO_USER_INFO_URI", dotenv.get("KAKAO_USER_INFO_URI"));

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_ACCESS_EXPIRE_MS", dotenv.get("JWT_ACCESS_EXPIRE_MS"));
		System.setProperty("JWT_REFRESH_EXPIRE_MS", dotenv.get("JWT_REFRESH_EXPIRE_MS"));




		SpringApplication.run(HeattechApplication.class, args);
	}

}
