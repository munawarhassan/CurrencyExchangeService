package com.ibm.currency.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurrencyExchangeBean;
import com.ibm.currency.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currencyexchange")
public class CurrencyExchangeController{
	
	/*@Value("${currencyexchangeservice.greetprop}")
	private String greetProp;*/
	
	@Autowired
	private CurencyExchangeConfig curencyExchangeConfig;

	
	@Autowired
	private CurrencyExchangeService currencyexchangeservice;
	
	@Autowired
	private CurrencyExchangeBean exchangeBean;
	
		
	@RequestMapping(path = "/default", method = RequestMethod.GET)
	public String getDefaultMessage() {
		return curencyExchangeConfig.getGreetProp();
		
		
	}

	@RequestMapping(path = "/convertcurrency", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrency(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservice.convertCurrency(currencyExchangeBean);		
  
	}
	
}