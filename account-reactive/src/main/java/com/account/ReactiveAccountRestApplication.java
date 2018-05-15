package com.account;

import java.time.Duration;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.account.domain.entities.Account;
import com.account.domain.repositories.ReactiveAccountRepository;
import com.mongodb.reactivestreams.client.MongoCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

  
/**
 * Created by rodrigo.chaves on 27/06/2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableReactiveMongoRepositories
public class ReactiveAccountRestApplication implements CommandLineRunner {
 
	@Autowired
	ReactiveAccountRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(ReactiveAccountRestApplication.class, args);
	}

	@Override 
	public void run(String... args) throws Exception {
		
		Flux<Account> accounts = Flux.range(1, 10000).map(i -> new Account(i));
		
		/*
		 * Generate e new Acccount every 5 seconds
		 */		
		Flux.interval(Duration.ofSeconds(5))  
			.zipWith(accounts) 
			.map(Tuple2::getT2)  
			.flatMap(repository::save) 			
			.subscribe(); 
	}

		
	@Bean
	public CommandLineRunner initData(ReactiveMongoOperations mongo) {
		return (String... args) -> {

			mongo.dropCollection(Account.class) //
					 				.then(mongo.createCollection(Account.class, CollectionOptions.empty() // 
					 						.size(1024 * 1024) // 
					 						.maxDocuments(100) // 
					 						.capped())).subscribe(); 
			
/*
			Flux.range(1, 10000).map(i -> new Account(i)).doOnNext(mongo::save).blockLast(Duration.ofSeconds(5));
*/			
		};
	}

}