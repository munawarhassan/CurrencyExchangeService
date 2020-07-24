package com.ibm.currency.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ibm.currency.model.CoreResponseModel;
import com.ibm.currency.model.CurrencyConversionFactor;
import com.ibm.currency.model.CurrencyExchangeBean;
@Service
@Component
public class CurrencyExchangeService{

	@Autowired
	private CurrencyConverterServiceProxy proxyservice;
	

	private ResponseEntity<?>  respEntity;
	private static Logger log = LoggerFactory.getLogger(CurrencyExchangeService.class);
	
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
	

}
