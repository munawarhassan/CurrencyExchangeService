package com.ibm.currency.model;

import org.springframework.stereotype.Component;

@Component
public class CurrencyExchangeBean extends CoreModel{
	
	private String fromcurrency;
	
	private String tocurrency;
	
	private Double currencyVal;
	
	//private Double conversionFactor;
	
	private Double convertedAmount;	
	
	boolean defaultpopulated;
	
	
	

	public String getFromcurrency() {
		return fromcurrency;
	}

	public void setFromcurrency(String fromcurrency) {
		this.fromcurrency = fromcurrency;
	}

	public String getTocurrency() {
		return tocurrency;
	}

	public void setTocurrency(String tocurrency) {
		this.tocurrency = tocurrency;
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

	/*
	 * public Double getConversionFactor() { return conversionFactor; }
	 * 
	 * public void setConversionFactor(Double conversionFactor) {
	 * this.conversionFactor = conversionFactor; }
	 */

	public boolean isDefaultpopulated() {
		return defaultpopulated;
	}

	public void setDefaultpopulated(boolean defaultpopulated) {
		this.defaultpopulated = defaultpopulated;
	}
	
	

	
}
