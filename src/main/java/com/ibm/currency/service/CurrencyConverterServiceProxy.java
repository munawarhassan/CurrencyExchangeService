package com.ibm.currency.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurrencyExchangeBean;


@FeignClient(name = "CurrencyConversionFactorService" , fallback = CurrencyConverterServiceFallback.class)
public interface CurrencyConverterServiceProxy {
	
	@RequestMapping(path = "/currencyconversionfactor/getconversionfactor", method = RequestMethod.POST, produces = {"application/json"})	
    public CurrencyExchangeBean getConversionFactor(@RequestBody CurrencyExchangeBean currencyExchangeBean); 
			   
	 
}
