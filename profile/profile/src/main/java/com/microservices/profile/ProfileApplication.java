package com.microservices.profile;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class ProfileApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProfileApplication.class, args);
		SecretKey key = Jwts.SIG.HS512.key().build();
		String secret = Encoders.BASE64.encode(key.getEncoded());

		System.out.println(secret);
	}

}
