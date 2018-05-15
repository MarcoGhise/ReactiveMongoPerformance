package com.account.rest;

import com.account.domain.entities.Account;
import com.account.domain.enums.Currency;
import com.account.domain.repositories.ReactiveAccountRepository;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Created by rodrigo.chaves on 20/06/2017.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
	
    @Autowired
    private ReactiveAccountRepository reactiveAccountRepository;

    @RequestMapping(value = "/currency/{currency}", method = RequestMethod.GET)
    Flux<Account> findByCurrency(@PathVariable String currency) {
    	
    	return reactiveAccountRepository.findByCurrency(Currency.fromValue(currency)).subscribeOn(Schedulers.elastic());
    }
    
    @RequestMapping(value = "/amount/{ammount}", method = RequestMethod.GET)
    Mono<Account> findByAmount(@PathVariable Double ammount) {
    	
        return reactiveAccountRepository.findByAmount(ammount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Mono<Account> findById(@PathVariable String id) {
        return reactiveAccountRepository.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Mono<Account> save(@RequestBody Account account) {
        return reactiveAccountRepository.save(account);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    Flux<Account> saveAll(@RequestBody Flux<Account> accounts) {
        return reactiveAccountRepository.saveAll(accounts);
    }  

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Flux<Account> findAll() {
    	  
        Flux<Account> account = reactiveAccountRepository.findAll();
          
        return account;
    }
    
    @RequestMapping(value = "/stream", method = RequestMethod.GET, produces=MediaType.TEXT_EVENT_STREAM_VALUE )
    Flux<Account> findAllStreaming() {
    	
        Flux<Account> account = reactiveAccountRepository.findAll().delayElements(Duration.ofSeconds(1));
         
        return account;
    }
    
}
