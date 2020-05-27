package com.ibm.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ibm.currency.model.CoreException;
import com.ibm.currency.model.CurrencyExchangeBean;


@Service
public class CurrencyExchangeService{
	@Autowired
	private CurrencyExchangeBean respModel;

	@Autowired

	private DiscoveryClient discoveryClient;
	private ResponseEntity<?>  respEntity;
	
	public ResponseEntity<?>  getConversionfactor(String countryCode, Double amount){
		try {
			
			return populateSuccessResponseWithResult(respModel, "Successfully saved records to database");
		} catch (Exception ex) {
		
			return populateFailureResponse("Failed to insert record"+ ex.getMessage());
		}
	}
	
	
	
	
public ResponseEntity<?>   populateSuccessResponseWithResult(CurrencyExchangeBean result, String message){
		
		/*respModel = new CoreResponseModel();
		respModel.setStatusCode(200);
		respModel.setMessage(message);
		respModel.setResponseBody(result);
		respEntity = new ResponseEntity<Object>(respModel,HttpStatus.OK);	*/	
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
