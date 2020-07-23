package com.ibm.currency.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CoreModel;
import com.ibm.currency.model.CoreResponseModel;
import com.ibm.currency.model.CurencyExchangeConfig;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurrencyExchangeBean;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
@Component
public class CurrencyExchangeService{

	@Autowired
	private CurrencyConverterServiceProxy proxyservice;
	

	private ResponseEntity<?>  respEntity;
	 
	 
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
	

}
