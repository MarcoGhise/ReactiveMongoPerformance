package com.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;

import com.account.domain.entities.Account;

@SpringBootApplication
public class ServletAccountRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServletAccountRestApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(MongoOperations mongo) {
		return (String... args) -> {

			mongo.dropCollection(Account.class);
			mongo.createCollection(Account.class);

			for (int i = 0; i < 10001; i++)
				mongo.save(new Account(i));
		};
	}

}
