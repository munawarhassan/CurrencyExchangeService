package com.ibm.currency.model;

import org.springframework.stereotype.Component;

@Component
public class CurrencyExchangeBean {
	
	private String countryCode;
	
	private double convertedAmount;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public double getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(double convertedAmount) {
		this.convertedAmount = convertedAmount;
	}
	
	

}
