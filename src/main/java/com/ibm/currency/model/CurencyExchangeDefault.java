package com.ibm.currency.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class CurencyExchangeDefault {
	
	private  final Double EUROconversionfactor = 1.15;
	private final Double INRconversionfactor = 0.017;
	private final Double AUDconversionfactor = 0.75;
	private final Double GBPconversionfactor =1.25;
	
	
	
	public CurencyExchangeDefault() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Double getEUROconversionfactor() {
		return EUROconversionfactor;
	}
	public Double getINRconversionfactor() {
		return INRconversionfactor;
	}
	public Double getAUDconversionfactor() {
		return AUDconversionfactor;
	}
	public Double getGBPconversionfactor() {
		return GBPconversionfactor;
	}
	
	
}
