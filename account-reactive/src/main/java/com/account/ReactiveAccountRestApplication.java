package com.account;

import java.time.Duration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.account.domain.entities.Account;

import reactor.core.publisher.Flux;

 
/**
 * Created by rodrigo.chaves on 27/06/2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableReactiveMongoRepositories
public class ReactiveAccountRestApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReactiveAccountRestApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(MongoOperations mongo) {
		return (String... args) -> {

			mongo.dropCollection(Account.class);
			mongo.createCollection(Account.class);

			Flux.range(1, 10000).map(i -> new Account(i)).doOnNext(mongo::save).blockLast(Duration.ofSeconds(5));
		};
	}

}