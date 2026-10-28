package com.jordan.skyshoplink;

import io.jsonwebtoken.Jwts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class ShoplinkApplication {


	public static void main(String[] args) {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String secretKeyString = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("votre cle de securité est : ");
		System.out.println(secretKeyString);
		SpringApplication.run(ShoplinkApplication.class, args);
	}

}
