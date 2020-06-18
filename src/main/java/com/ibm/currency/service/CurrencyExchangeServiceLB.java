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
@RibbonClient(name = "CurrencyConversionFactorService")
public class CurrencyExchangeServiceLB{

	@Autowired
	private CoreResponseModel respModel;	
	
	@Autowired
	private LoadBalancerClient lbClient;

	@Autowired
	private CurencyExchangeConfig curencyExchangeConfig;
	
	  
   	@Autowired
	@Lazy
	RestTemplate lbrestTemplate;

	 private ResponseEntity<?>  respEntity;
	
	public ResponseEntity<?>  convertCurrency(CurrencyExchangeBean currencyExchangeBean){
		try {			
			
			ServiceInstance instance = lbClient.choose("CurrencyConversionFactorService");
			String baseUrl = "http://" + instance.getHost() + ":" + instance.getPort() + "/currencyconversionfactor/getconversionfactor";
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);
			ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST,
					httpEntity, CurrencyExchangeBean.class);		
		
			currencyExchangeBean = populateXngBean(currencyExchangeBean,responseEntity);
			return populateSuccessResponseWithResult(currencyExchangeBean, responseEntity.getBody().getMessage() +" "+ currencyExchangeBean.getCountryCode());
		} catch (Exception ex) {
		
			return populateFailureResponse("Failed to convert currency as no record found");
		}
	}
	
	@HystrixCommand(fallbackMethod = "getDefaultConversionFactor")
	public ResponseEntity<?>  convertCurrency_WithFallBack(CurrencyExchangeBean currencyExchangeBean){
		//try {			
			String baseUrl = "http://CurrencyConversionFactorService/currencyconversionfactor/getconversionfactor";			
			HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);
			ResponseEntity<CurrencyExchangeBean> responseEntity = lbrestTemplate.exchange(baseUrl, HttpMethod.POST,
					httpEntity, CurrencyExchangeBean.class);
			currencyExchangeBean = populateXngBean(currencyExchangeBean,responseEntity);
			return populateSuccessResponseWithResult(currencyExchangeBean, responseEntity.getBody().getMessage() +" "+ currencyExchangeBean.getCountryCode());
		//} catch (Exception ex) {
		
			//return populateFailureResponse("Failed to convert currency as no record found");
		//}
	}
	
	public ResponseEntity<?> getDefaultConversionFactor(CurrencyExchangeBean currencyExchangeBean){	
		
		CurrencyExchangeBean bean = new CurrencyExchangeBean();
		
		if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("EUR")) {
			bean.setConversionFactor(curencyExchangeConfig.getEUROconversionfactor());			
		}else if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("INR")) {
			bean.setConversionFactor(curencyExchangeConfig.getINRconversionfactor());			
		}else if(currencyExchangeBean.getCountryCode().equalsIgnoreCase("AUD")) {
			bean.setConversionFactor(curencyExchangeConfig.getAUDconversionfactor());
		}
		bean.setMessage("Converter Service Down ... converted with DEFAULT RATE  from USD to");
		//ResponseEntity<CurrencyExchangeBean> response = new ResponseEntity<CurrencyExchangeBean>(bean,HttpStatus.NOT_FOUND);
		//return bean;
		
		
		respModel = new CoreResponseModel();
		respModel.setStatusCode(200);
		respModel.setMessage("Converter Service Down ... converted with DEFAULT RATE  from USD to");
		respModel.setResponseBody(bean);
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);
		return respEntity;
		
	}
	
	
	private CurrencyExchangeBean populateXngBean(CurrencyExchangeBean currencyExchangeBean,ResponseEntity<CurrencyExchangeBean> responseEntity) {

		Double conversionfactor = responseEntity.getBody().getConversionFactor();
		Double currencyVal = currencyExchangeBean.getCurrencyVal();
		Double convertedAmount = currencyVal * conversionfactor ;
		currencyExchangeBean.setConvertedAmount(convertedAmount);
		currencyExchangeBean.setConversionFactor(conversionfactor);
		return currencyExchangeBean;
	}
	
public ResponseEntity<?>   populateSuccessResponseWithResult(CurrencyExchangeBean result, String message){
		
		respModel = new CoreResponseModel();
		respModel.setStatusCode(200);
		respModel.setMessage(message);
		respModel.setResponseBody(result);
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);
		return respEntity;
	}

public ResponseEntity<?>  populateFailureResponse( String message){	
	respModel = new CoreResponseModel();
	respModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
	respModel.setSuccess(false);
	respModel.setMessage(message);		
	respEntity = new ResponseEntity<Object>(respModel,HttpStatus.BAD_REQUEST);		
	return respEntity;
}

}
