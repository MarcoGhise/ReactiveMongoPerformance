package com.account.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import com.account.domain.entities.Account;
import com.account.domain.repositories.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServletAccountRestApplicationTest {

	private final static Logger logger = LoggerFactory.getLogger(ServletAccountRestApplicationTest.class);
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
	private MongoOperations mongo;
	
	private static final StopWatch watch = new StopWatch();
	
	private static final List<Account> accounts = new ArrayList<Account>();
	
	private static List<Account> accountsDb = new ArrayList<Account>();
	
	//@Before
	public void setup()
	{
		mongo.dropCollection(Account.class);
		mongo.createCollection(Account.class);
		
		for (int i = 0; i < 10001; i++)
			accounts.add(new Account(i));		
	}
	
	@Test
	@Ignore
	public void write()
	{
		executeWatched(() -> accountRepository.saveAll(accounts));
		
		logger.info(String.format("Executed Time:%sms", watch.getLastTaskTimeMillis()));
	}
	
	@Test
	public void read() throws InterruptedException
	{
		accountsDb = executeWatched(accountRepository::findAll);
		
		logger.info(String.format("Executed Time:%sms", watch.getLastTaskTimeMillis()));
		
		assertEquals(accountsDb.size(), 10001);
	}
	
	private interface WatchCallback<T> {
		T doInWatch();
	}
	
	private <T> T executeWatched(WatchCallback<T> callback) {

		watch.start();

		try {
			return callback.doInWatch();
		} finally {
			watch.stop();
		}
	}
}
