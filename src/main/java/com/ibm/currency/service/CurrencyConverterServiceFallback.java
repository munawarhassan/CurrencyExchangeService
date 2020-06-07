package com.ibm.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurrencyExchangeBean;

@Component
public class CurrencyConverterServiceFallback implements CurrencyConverterServiceProxy {

	@Autowired
	private CurencyExchangeConfig curencyExchangeConfig;
	
	@Override
	public CurrencyExchangeBean getConversionFactor(CurrencyExchangeBean currencyExchangeBean){
		// TODO Auto-generated method stub
		CurrencyExchangeBean bean = new CurrencyExchangeBean();
		bean.setMessage("N");	
		if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("EUR")) {
			bean.setConversionFactor(curencyExchangeConfig.getEUROconversionfactor());			
		}else if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("INR")) {
			bean.setConversionFactor(curencyExchangeConfig.getINRconversionfactor());			
		}else if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("AUD")) {
			bean.setConversionFactor(curencyExchangeConfig.getAUDconversionfactor());
		}
		bean.setMessage("Convertet Service Down ... converted with DEFAULT RATE  from USD to");
		return bean;
	}
	
	

}
