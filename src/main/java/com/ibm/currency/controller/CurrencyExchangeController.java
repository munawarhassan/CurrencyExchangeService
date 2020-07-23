package com.ibm.currency.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurrencyExchangeBean;
import com.ibm.currency.service.CurrencyExchangeService;
import com.ibm.currency.service.CurrencyExchangeServiceLB;

@RestController
@RequestMapping("/currencyexchange")
public class CurrencyExchangeController{
	
	/*
	 * @Value("${currencyexchangeservice.greetprop}") private String greetProp1;
	 */
	
	@Autowired
	private CurencyExchangeConfig curencyExchangeConfig;

	
	@Autowired
	private CurrencyExchangeService currencyexchangeservice;
	

	@RequestMapping(path = "/default", method = RequestMethod.GET)
	public String getDefaultMessage() {
		return curencyExchangeConfig.getGreetProp();
		//return greetProp1;		
		
	}	
	@RequestMapping(path = "/convertcurrency/{amount}/{fromcurrency}/{tocurrency}", method = RequestMethod.GET, produces = {"application/json"})	
    public ResponseEntity<?> convertToDesiredCurrency(@PathVariable double amount, @PathVariable String fromcurrency, @PathVariable String tocurrency ) throws CoreException{ 
			
		CurrencyExchangeBean crexngbean = new CurrencyExchangeBean();
		crexngbean.setCurrencyVal(amount);
		crexngbean.setFromcurrency(fromcurrency);
		crexngbean.setTocurrency(tocurrency);
		return currencyexchangeservice.convertCurrency_FC(crexngbean);	
	
  
	}
	
	
	
	
	
	
}