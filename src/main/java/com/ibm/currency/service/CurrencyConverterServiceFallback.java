package com.ibm.currency.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurencyExchangeDefault;

@Component
public class CurrencyConverterServiceFallback implements CurrencyConverterServiceProxy {

	
	@Autowired private CurencyExchangeConfig curencyExchangeConfig;	 
	
	//@Autowired private CurencyExchangeDefault curencyExchangeConfig;
	
	private static Logger log = LoggerFactory.getLogger(CurrencyConverterServiceFallback.class);
	
	@Override
	public CurrencyConversionFactor getConversionFactor(String currency) throws CoreException{
		// TODO Auto-generated method stub
		
		log.info("Fallback Method called");
		CurrencyConversionFactor defaultFactor = new CurrencyConversionFactor();		
		if(currency.equalsIgnoreCase("EUR")) {
			defaultFactor.setConversionFactor(curencyExchangeConfig.getEUROconversionfactor());			
		}else if(currency.equalsIgnoreCase("INR")) {
			defaultFactor.setConversionFactor(curencyExchangeConfig.getINRconversionfactor());			
		}else if(currency.equalsIgnoreCase("AUD")) {
			defaultFactor.setConversionFactor(curencyExchangeConfig.getAUDconversionfactor());
		}else if(currency.equalsIgnoreCase("GBP")) {
			defaultFactor.setConversionFactor(curencyExchangeConfig.getGBPconversionfactor());
		}
		
		defaultFactor.setDefaultpopulated(true);
		return defaultFactor;
	}
	
	

}
