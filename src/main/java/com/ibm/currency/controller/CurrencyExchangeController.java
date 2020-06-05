package com.ibm.currency.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurrencyExchangeBean;
import com.ibm.currency.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currencyexchange")
public class CurrencyExchangeController{
	
	@Autowired
	private CurrencyExchangeService currencyexchangeservice;
	
	@Autowired
	private CurrencyExchangeBean exchangeBean;

	
	@RequestMapping(path = "/default", method = RequestMethod.GET)
	public String getDefaultMessage() {
		return "I am Ready for currency Exchange assignment with microservic22";
	}

	@RequestMapping(path = "/convertcurrency", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrency(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservice.convertCurrency(currencyExchangeBean);		
  
	}
	
	

	/*@RequestMapping(path = "/getconversionfactor", method = RequestMethod.GET, produces = {"application/json"})	
    public ResponseEntity<?> getConversionFactor(@RequestParam String countryCode) throws CoreException{ 
			
		return currencyservice.getConversionfactor(countryCode);
		
    }*/
	
	/*@RequestMapping(path = "/convertcurrency", method = RequestMethod.GET, produces = {"application/json"})	
   
	private ResponseEntity<?> convertCurrency(@RequestParam (value = "countryCode") String countryCode , @RequestParam (value = "currencyVal") Double currencyVal) {
			
		exchangeBean.setCurrencyVal(currencyVal);
		exchangeBean.setCountryCode(countryCode);
		return currencyexchangeservice.convertCurrency(exchangeBean);
		
    }*/
	
}