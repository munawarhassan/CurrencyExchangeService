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
	
	@Autowired
	private CurrencyExchangeServiceLB currencyexchangeservicelb;

	@RequestMapping(path = "/default", method = RequestMethod.GET)
	public String getDefaultMessage() {
		return curencyExchangeConfig.getGreetProp();
		//return greetProp1;
		
		
	}	
	
	@RequestMapping(path = "/convertcurrency/withoutclient", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrencyWLB(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservice.convertCurrency_NoClient(currencyExchangeBean);		
  
	}
	
	
	@RequestMapping(path = "/convertcurrency/usingrdiscovery", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrencyUsingDiscoveryClient(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservice.convertCurrency_DC(currencyExchangeBean);		
  
	}
	
	@RequestMapping(path = "/convertcurrency/usingfeign", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrencyUsingFeignClient(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservicelb.convertCurrency_FC(currencyExchangeBean);		
  
	}
	
	@RequestMapping(path = "/convertcurrency/usingribbon", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrencyUsingRibbonClient(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservicelb.convertCurrency_RB(currencyExchangeBean);		
  
	}
	
	@RequestMapping(path = "/convertcurrency/usingribbonwithfallback", method = RequestMethod.POST, produces = {"application/json"})	
    public ResponseEntity<?> convertCurrency(@RequestBody CurrencyExchangeBean currencyExchangeBean ) throws CoreException{ 
			
		return currencyexchangeservicelb.convertCurrency_RBWithFallBack(currencyExchangeBean);	
	
  
	}
	
	
}