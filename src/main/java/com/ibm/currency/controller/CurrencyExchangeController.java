package com.ibm.currency.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currencyexchange")
public class CurrencyExchangeController{
	
	@Autowired
	private CurrencyExchangeService currencyexchangeservice;
	@RequestMapping(path = "/default", method = RequestMethod.GET)
	public String getDefaultMessage() {
		return "I am Ready for currency Exchange assignment with microservic22";
	}

	@RequestMapping(path = "/getconvertedamount", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> getConversionFactor(@RequestBody String countryCode, Double amount ) throws CoreException{ 
			
		return currencyexchangeservice.getConversionfactor(countryCode,amount);
		
  
}
}