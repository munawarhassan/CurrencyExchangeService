package com.ibm.currency.service;

import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CoreModel;
import com.ibm.currency.model.CoreResponseModel;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurrencyExchangeBean;


@Service
public class CurrencyExchangeService{

	@Autowired
	private CoreResponseModel respModel;
	
	@Autowired
	private RestTemplate restTemplate1;
	
	private ResponseEntity<?>  respEntity;
	@Autowired
	private CurrencyConverterServiceProxy proxyservice;
	
	public ResponseEntity<?>  convertCurrency(CurrencyExchangeBean currencyExchangeBean){
		try {
			
			
			//String url = "http://localhost:8081/currencyconversionfactor/getconversionfactor?countryCode="+currencyExchangeBean.getCountryCode();
			//String url = "http://CurrencyService1/currencyconversionfactor/getconversionfactor?countryCode="+currencyExchangeBean.getCountryCode();
			//ResponseEntity<CurrencyExchangeBean> responseEntity = restTemplate1.getForEntity(url, CurrencyExchangeBean.class);
			CurrencyExchangeBean responseBean = proxyservice.getConversionFactor(currencyExchangeBean);
			
			Double currencyVal = currencyExchangeBean.getCurrencyVal();
			Double conversionfactor = responseBean.getConversionFactor();
			Double convertedAmount = currencyVal * conversionfactor ;
			currencyExchangeBean.setConvertedAmount(convertedAmount);
			currencyExchangeBean.setConversionFactor(conversionfactor);
			return populateSuccessResponseWithResult(currencyExchangeBean, "Successfully Coverted USD to "+ currencyExchangeBean.getCountryCode());
		} catch (Exception ex) {
		
			return populateFailureResponse("Failed to convert currency"+ ex.getMessage());
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
	/*respModel = new CoreResponseModel();
	respModel.setStatusCode(HttpStatus.BAD_REQUEST.value());
	respModel.setSuccess(false);
	respModel.setMessage(message);		
	respEntity = new ResponseEntity<Object>(respModel,HttpStatus.BAD_REQUEST);		*/
	return respEntity;
}

}
