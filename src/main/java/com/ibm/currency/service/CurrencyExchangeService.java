package com.ibm.currency.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.currency.model.CoreResponseModel;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurencyExchangeDefault;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurrencyExchangeBean;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
@Service
@Component
@RibbonClient(name = "currencyconversionfactorservice")
public class CurrencyExchangeService{

	@Autowired
	private CurrencyConverterServiceProxy proxyservice;
	
	
	@Bean
	@LoadBalanced
	RestTemplate createRestTemplate() {
		RestTemplateBuilder b = new RestTemplateBuilder();
		return b.build();
	}

	@Autowired
	@Lazy
	RestTemplate lbrestTemplate;

	private ResponseEntity<?>  respEntity;
	private static Logger log = LoggerFactory.getLogger(CurrencyExchangeService.class);
	
	//@Autowired private CurencyExchangeConfig curencyExchangeConfig;	 
	
	@Autowired private CurencyExchangeDefault curencyExchangeConfig;
	
   	public ResponseEntity<?>  convertCurrency_FC(CurrencyExchangeBean currencyExchangeBean){
		try {
			CurrencyConversionFactor fromcurrencyFactor = null;
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getFromcurrency()))) {
				fromcurrencyFactor = proxyservice.getConversionFactor(currencyExchangeBean.getFromcurrency());
			}else {
				fromcurrencyFactor = new CurrencyConversionFactor();
				fromcurrencyFactor.setConversionFactor(1.00);
			}
			
			CurrencyConversionFactor tocurrencyFactor = null;
			Double reqdConvertedAmount = null;
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getTocurrency()))) {
				tocurrencyFactor = proxyservice.getConversionFactor(currencyExchangeBean.getTocurrency());	
				reqdConvertedAmount= (currencyExchangeBean.getCurrencyVal()) * ((fromcurrencyFactor.getConversionFactor())/(tocurrencyFactor.getConversionFactor()));
			}else {
				reqdConvertedAmount = (currencyExchangeBean.getCurrencyVal()) * (fromcurrencyFactor.getConversionFactor());
			}
			
			currencyExchangeBean.setConvertedAmount (reqdConvertedAmount);
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getFromcurrency()))) {
				currencyExchangeBean.setDefaultpopulated(fromcurrencyFactor.isDefaultpopulated());
			}else {
				currencyExchangeBean.setDefaultpopulated(tocurrencyFactor.isDefaultpopulated());
			}
			log.info("caurrency Value converted ");
			return populateSuccessResponseWithResult(currencyExchangeBean);		
		} catch (Exception ex) {			
			return populateFailureResponse("Failed to convert currency as no record found");
	
	}
		
  }
   
   	
public ResponseEntity<?>   populateSuccessResponseWithResult(CurrencyExchangeBean currencyExchangeBean){
		CoreResponseModel respModel = new CoreResponseModel();
		respModel.setStatusCode(200);		
		if(currencyExchangeBean.isDefaultpopulated()) {
			respModel.setMessage("Converter Service Down ... converted with DEFAULT RATE  from " +currencyExchangeBean.getFromcurrency()+" to  "+ currencyExchangeBean.getTocurrency());
		}else {
			respModel.setMessage("Successfully Coverted With Present Rate  from " +currencyExchangeBean.getFromcurrency()+" to "+ currencyExchangeBean.getTocurrency());
		}
		respModel.setResponseBody(currencyExchangeBean);
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);
		return respEntity;
	}

	public ResponseEntity<?>  populateFailureResponse( String message){	
		CoreResponseModel respModel = new CoreResponseModel();
		respModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
		respModel.setMessage(message);
		respModel.setSuccess(false);				
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.BAD_REQUEST);		
		return respEntity;
	}
	
	/*
	 * @HystrixCommand(commandProperties = {
	 * 
	 * @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	 * value = "100") } ,fallbackMethod = "getDefaultConversionFactor")
	 */
	@HystrixCommand(fallbackMethod = "getDefaultConversionFactor")
	public ResponseEntity<?>  convertCurrency_RBWithFallBack(CurrencyExchangeBean currencyExchangeBean){
		
			String baseUrl = "http://currencyconversionfactorservice/currencyconversionfactor/getconversionfactor/{currency}";	
			
		//	String baseUrl = "http://CurrencyConversionFactorService/currencyconversionfactor/getconversionfactor";			
			/*HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);
			ResponseEntity<CurrencyConversionFactor> responseEntity = lbrestTemplate.exchange(baseUrl, HttpMethod.GET,
					httpEntity, CurrencyConversionFactor.class);
			currencyExchangeBean.setConversionFactor(responseEntity.getBody().getConversionFactor());	*/
	
			CurrencyConversionFactor fromcurrencyFactor = null;
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getFromcurrency()))) {
				  Map<String, String> params = new HashMap<String, String>();
				    params.put("currency", currencyExchangeBean.getFromcurrency());	
				    fromcurrencyFactor = lbrestTemplate.getForObject(baseUrl, CurrencyConversionFactor.class, params);
			}else {
				fromcurrencyFactor = new CurrencyConversionFactor();
				fromcurrencyFactor.setConversionFactor(1.00);
			}
			
			CurrencyConversionFactor tocurrencyFactor = null;
			Double reqdConvertedAmount = null;
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getTocurrency()))) {
				
				  Map<String, String> params = new HashMap<String, String>();
				    params.put("currency", currencyExchangeBean.getTocurrency());				     
				   tocurrencyFactor = lbrestTemplate.getForObject(baseUrl, CurrencyConversionFactor.class, params);
				//reqdConvertedAmount= (currencyExchangeBean.getCurrencyVal()) * ((fromcurrencyFactor.getConversionFactor())/(tocurrencyFactor.getConversionFactor()));
			}else {
				tocurrencyFactor = new CurrencyConversionFactor();
				tocurrencyFactor.setConversionFactor(1.00);
				//reqdConvertedAmount = (currencyExchangeBean.getCurrencyVal()) * (fromcurrencyFactor.getConversionFactor());
			}
			
			
			
			reqdConvertedAmount= (currencyExchangeBean.getCurrencyVal()) * ((fromcurrencyFactor.getConversionFactor())/(tocurrencyFactor.getConversionFactor()));
			
			
			currencyExchangeBean.setConvertedAmount (reqdConvertedAmount);
			if(!("USD".equalsIgnoreCase(currencyExchangeBean.getFromcurrency()))) {
				currencyExchangeBean.setDefaultpopulated(fromcurrencyFactor.isDefaultpopulated());
			}else {
				currencyExchangeBean.setDefaultpopulated(tocurrencyFactor.isDefaultpopulated());
			}
			log.info("caurrency Value converted ");			
			return populateSuccessResponseWithResult(currencyExchangeBean);
			
	}
	
	
	public ResponseEntity<?> getDefaultConversionFactor(CurrencyExchangeBean currencyExchangeBean){
    	  log.info("Fallback Method called");
    	  CurrencyConversionFactor fromcurrencyFactor = new CurrencyConversionFactor();
    	  fromcurrencyFactor.setConversionFactor(getDefaultValue(currencyExchangeBean.getFromcurrency()));
    	  CurrencyConversionFactor tocurrencyFactor =  new CurrencyConversionFactor();
		  tocurrencyFactor.setConversionFactor(getDefaultValue(currencyExchangeBean.getTocurrency()));
		  Double reqdConvertedAmount = null;
			  
			  try {
			  reqdConvertedAmount= (currencyExchangeBean.getCurrencyVal()) * ((fromcurrencyFactor.getConversionFactor())/(tocurrencyFactor.getConversionFactor()));
			  currencyExchangeBean.setConvertedAmount (reqdConvertedAmount);
			  currencyExchangeBean.setDefaultpopulated(true);			
				CoreResponseModel respModel = new CoreResponseModel();				
				respModel.setMessage("Converter Service Down ... converted with DEFAULT RATE  from " +currencyExchangeBean.getFromcurrency()+" to  "+ currencyExchangeBean.getTocurrency());
				respModel.setResponseBody(currencyExchangeBean);		
				respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);	
				return respEntity;
			  }catch (Exception ex) {		
					
					return populateFailureResponse("For Both/One of  the currency Data not availabe and Don't have Default value ");
					
				}
			
		 
		 	
	}
      
      
	private  Double getDefaultValue(String currency) {
		
		 Double value = null;
		
			  if(currency.equalsIgnoreCase("EUR")) {				  
				  value = curencyExchangeConfig.getEUROconversionfactor();
			  }else if(currency.equalsIgnoreCase("INR")) {
				  value = curencyExchangeConfig.getINRconversionfactor();
			  }else if(currency.equalsIgnoreCase("AUD")) {
				  value = curencyExchangeConfig.getAUDconversionfactor();
			  }else if(currency.equalsIgnoreCase("GBP")) {
				  value = curencyExchangeConfig.getGBPconversionfactor();
			  }else if(currency.equalsIgnoreCase("USD")) {
				  value = 1.00;
			  }
		return value;
	}
	
	
	/*
	 * public ResponseEntity<?> getDefaultConversionFactor(CurrencyExchangeBean
	 * currencyExchangeBean){
	 * 
	 * if(currency.equalsIgnoreCase("EUR")) {
	 * defaultFactor.setConversionFactor(curencyExchangeConfig.
	 * getEUROconversionfactor()); }else if(currency.equalsIgnoreCase("INR")) {
	 * defaultFactor.setConversionFactor(curencyExchangeConfig.
	 * getINRconversionfactor()); }else if(currency.equalsIgnoreCase("AUD")) {
	 * defaultFactor.setConversionFactor(curencyExchangeConfig.
	 * getAUDconversionfactor()); }else if(currency.equalsIgnoreCase("GBP")) {
	 * defaultFactor.setConversionFactor(curencyExchangeConfig.
	 * getAUDconversionfactor()); }
	 * 
	 * 
	 * Double conversionfactor = currencyExchangeBean.getConversionFactor(); Double
	 * currencyVal = currencyExchangeBean.getCurrencyVal(); Double convertedAmount =
	 * currencyVal * conversionfactor ;
	 * currencyExchangeBean.setConvertedAmount(convertedAmount); CoreResponseModel
	 * respModel = new CoreResponseModel(); respModel.
	 * setMessage("Converter Service Down ... converted with DEFAULT RATE  from USD to"
	 * ); respModel.setResponseBody(currencyExchangeBean); respEntity = new
	 * ResponseEntity<Object>(respModel,HttpStatus.OK); return respEntity;
	 * 
	 * }
	 */
	 
	
	

}
