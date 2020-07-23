package com.ibm.currency.model;

public class CurrencyConversionFactor extends CoreModel{
	
	private String countryCode;
	private Double conversionFactor;
	private int id;
	private String currency;
	boolean defaultpopulated;
	
	public CurrencyConversionFactor() {
		super();
	}
	public CurrencyConversionFactor(String countryCode, Double conversionFactor, int id, String currency) {		
		this.countryCode = countryCode;
		this.conversionFactor = conversionFactor;
		this.id = id;
		this.currency = currency;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Double getConversionFactor() {
		return conversionFactor;
	}
	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isDefaultpopulated() {
		return defaultpopulated;
	}
	public void setDefaultpopulated(boolean defaultpopulated) {
		this.defaultpopulated = defaultpopulated;
	}


}
