package com.ibm.currency.model;

public class CurrencyConversionFactor extends CoreModel{
	
   
     
	
	private String countryCode;
	private Double conversionFactor;
	
	
	
	public CurrencyConversionFactor() {
		super();
	}
	public CurrencyConversionFactor(String countryCode, Double conversionFactor) {		
		this.countryCode = countryCode;
		this.conversionFactor = conversionFactor;
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
	
	
	
	

}
