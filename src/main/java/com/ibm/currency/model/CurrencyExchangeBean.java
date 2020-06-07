package com.ibm.currency.model;

import org.springframework.stereotype.Component;

@Component
public class CurrencyExchangeBean extends CoreModel{
	
	private String countryCode;
	
	private Double currencyVal;
	
	private Double conversionFactor;
	
	private Double convertedAmount;
	
	private String message;
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Double getCurrencyVal() {
		return currencyVal;
	}

	public void setCurrencyVal(Double currencyVal) {
		this.currencyVal = currencyVal;
	}

	public Double getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(Double convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public Double getConversionFactor() {
		return conversionFactor;
	}

	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

	
}
