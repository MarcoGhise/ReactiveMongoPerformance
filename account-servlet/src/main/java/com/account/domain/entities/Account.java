package com.account.domain.entities;

import com.account.domain.enums.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "accounts")
public class Account {

	@Id
	private String id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date creationDate;

	private Double amount;

	private Currency currency;

	public Account() {
	}

	public Account(int i) {

		this.amount = Double.valueOf(i);
		this.creationDate = new Date(System.currentTimeMillis());
		if ((i % 2) == 0)
			this.currency = Currency.EUR;
		else if ((i % 3) == 0)
			this.currency = Currency.USD;
		else
			this.currency = Currency.BR;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Double getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}