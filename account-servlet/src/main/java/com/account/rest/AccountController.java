package com.account.rest;

import com.account.domain.entities.Account;
import com.account.domain.enums.Currency;
import com.account.domain.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @RequestMapping(value = "/currency/{currency}", method = RequestMethod.GET)
    List<Account> findByCurrency(@PathVariable String currency) {
    	return accountRepository.findByCurrency(Currency.fromValue(currency));
    }
    
    @RequestMapping(value = "/amount/{ammount}", method = RequestMethod.GET)
    Account findByCurrency(@PathVariable Double ammount) {
    	
        return accountRepository.findByAmount(ammount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    Optional<Account> findById(@PathVariable String id) {
        return accountRepository.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    Account save(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    List<Account> saveAll(@RequestBody List<Account> accounts) {
        return accountRepository.saveAll(accounts);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    List<Account> findAll() {
    	
    	long start = System.currentTimeMillis();
    	
        List<Account> account = accountRepository.findAll();
        
        long stop = System.currentTimeMillis() - start;
        
        logger.info("Execution time:" + stop + "ms");
        
        return account;
    }
    
}
