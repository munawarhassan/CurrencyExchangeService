package com.ibm.currency.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("currencyexchangeservice")
public class CurencyExchangeConfig {
	
	private Double EUROconversionfactor;
	private Double INRconversionfactor;
	private Double AUDconversionfactor;
	private String greetProp;
	public Double getEUROconversionfactor() {
		return EUROconversionfactor;
	}
	public void setEUROconversionfactor(Double eUROconversionfactor) {
		EUROconversionfactor = eUROconversionfactor;
	}
	public Double getINRconversionfactor() {
		return INRconversionfactor;
	}
	public void setINRconversionfactor(Double iNRconversionfactor) {
		INRconversionfactor = iNRconversionfactor;
	}
	public Double getAUDconversionfactor() {
		return AUDconversionfactor;
	}
	public void setAUDconversionfactor(Double aUDconversionfactor) {
		AUDconversionfactor = aUDconversionfactor;
	}
	public String getGreetProp() {
		return greetProp;
	}
	public void setGreetProp(String greetProp) {
		this.greetProp = greetProp;
	}
	
   
}
