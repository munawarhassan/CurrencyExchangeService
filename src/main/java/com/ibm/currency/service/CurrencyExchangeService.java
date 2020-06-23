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
	private DiscoveryClient discoveryClient;	

	private ResponseEntity<?>  respEntity;
	
   	@Autowired
	@Lazy
	RestTemplate restTemplate;
	
	public ResponseEntity<?>  convertCurrency_NoClient(CurrencyExchangeBean currencyExchangeBean){
		try {			
			
			//String baseUrl = "http://localhost:60464/currencyconversionfactor/getconversionfactor";
			String baseUrl = "http://CurrencyConversionFactorService/currencyconversionfactor/getconversionfactor";
			HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);			
			ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST,
					httpEntity, CurrencyExchangeBean.class);
			currencyExchangeBean.setConversionFactor(responseEntity.getBody().getConversionFactor());					
			//ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate1.getForEntity(baseUrl, CurrencyExchangeBean.class);// For Get Request
			return populateSuccessResponseWithResult(currencyExchangeBean);
			
		} catch (Exception ex) {
		
			return populateFailureResponse(currencyExchangeBean);
		}
	}
	
	
	public ResponseEntity<?>  convertCurrency_DC(CurrencyExchangeBean currencyExchangeBean){
		try {
			
			List<ServiceInstance> instances = discoveryClient.getInstances("CurrencyConversionFactorService");
			System.out.println("Instances of CurrencyConversionFactorService found =" + instances.size());
			for (ServiceInstance instance : instances) {
				System.out.println(instance.getHost() + ":" + instance.getPort());
			}
			ServiceInstance instance = instances.get(0);
			String baseUrl = "http://" + instance.getHost() + ":" + instance.getPort() + "/currencyconversionfactor/getconversionfactor";			
			System.out.println("baseurl ="+ baseUrl);			
			RestTemplate restTemplate1 = new RestTemplate();
			HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);
			ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate1.exchange(baseUrl, HttpMethod.POST,
					httpEntity, CurrencyExchangeBean.class);
			currencyExchangeBean.setConversionFactor(responseEntity.getBody().getConversionFactor());			
			return populateSuccessResponseWithResult(currencyExchangeBean);
		} catch (Exception ex) {
		
			return populateFailureResponse(currencyExchangeBean);
		}
	}
	
	
	public ResponseEntity<?>   populateSuccessResponseWithResult(CurrencyExchangeBean currencyExchangeBean){
		
			Double conversionfactor = currencyExchangeBean.getConversionFactor();
			Double currencyVal = currencyExchangeBean.getCurrencyVal();
			Double convertedAmount = currencyVal * conversionfactor ;
			currencyExchangeBean.setConvertedAmount(convertedAmount);
			CoreResponseModel respModel = new CoreResponseModel();
			respModel.setMessage("Successfully Coverted With Present Rate from USD to" +" "+ currencyExchangeBean.getCountryCode());		
			respModel.setStatusCode(200);			
			respModel.setResponseBody(currencyExchangeBean);
			respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);
			return respEntity;
		}
	
	public ResponseEntity<?>  populateFailureResponse(CurrencyExchangeBean currencyExchangeBean){	
		CoreResponseModel respModel = new CoreResponseModel();	
		respModel = new CoreResponseModel();
		respModel.setMessage("Failed to convert currency as no record found");
		respModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
		respModel.setSuccess(false);			
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.BAD_REQUEST);		
		return respEntity;
	}

}
