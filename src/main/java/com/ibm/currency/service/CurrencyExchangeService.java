package com.ibm.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
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
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurrencyExchangeBean;
@Service
@Component
@RibbonClient(name = "CurrencyConversionFactorService")
public class CurrencyExchangeService{

	@Autowired
	private CoreResponseModel respModel;
	
	@Autowired
	private RestTemplate restTemplate1;
	
	private ResponseEntity<?>  respEntity;
	@Autowired
	private CurrencyConverterServiceProxy proxyservice;
	
	@Autowired
	private LoadBalancerClient lbClient;
	
	public ResponseEntity<?>  convertCurrency(CurrencyExchangeBean currencyExchangeBean){
		try {
			
			
			//String url = "http://localhost:8081/currencyconversionfactor/getconversionfactor?countryCode="+currencyExchangeBean.getCountryCode();
			//String url = "http://CurrencyService1/currencyconversionfactor/getconversionfactor?countryCode="+currencyExchangeBean.getCountryCode();
			//ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate1.getForEntity(url, CurrencyExchangeBean.class);
			//CurrencyExchangeBean responseBean = proxyservice.getConversionFactor(currencyExchangeBean);
			
			ServiceInstance instance = lbClient.choose("CurrencyConversionFactorService");
			String baseUrl = "http://" + instance.getHost() + ":" + instance.getPort() + "/currencyconversionfactor/getconversionfactor";			
			System.out.println("baseurl ="+ baseUrl);
			//ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate1.getForEntity(baseUrl, CurrencyExchangeBean.class);
			//Double conversionfactor = responseEntity.getBody().getConversionFactor();
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<CurrencyExchangeBean> httpEntity = new HttpEntity<CurrencyExchangeBean>(currencyExchangeBean);
			ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST,
					httpEntity, CurrencyExchangeBean.class);		
			
			Double conversionfactor = responseEntity.getBody().getConversionFactor();
			Double currencyVal = currencyExchangeBean.getCurrencyVal();
			Double convertedAmount = currencyVal * conversionfactor ;
			currencyExchangeBean.setConvertedAmount(convertedAmount);
			currencyExchangeBean.setConversionFactor(conversionfactor);
			String message = responseEntity.getBody().getMessage();
			return populateSuccessResponseWithResult(currencyExchangeBean, message +" "+ currencyExchangeBean.getCountryCode());
		} catch (Exception ex) {
		
			return populateFailureResponse("Failed to convert currency as no record found");
		}
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
