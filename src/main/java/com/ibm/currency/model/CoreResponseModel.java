package com.ibm.currency.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import com.ibm.currency.model.CoreModel;


@Component
public class CoreResponseModel{
	
 @Transient
	private int statusCode;
	
 @Transient
	private boolean success = true;
	
 @Transient
  private String message;

 @Transient
	private CoreModel responseBody;
 
 @Transient
	private String path;
	
/* @Transient
	private ArrayList<CoreModel> resBodyList;
	

	
 @Transient
	private String responseBodyStr;
	
 @Transient
	private ArrayList<String> respBodyListOfString;
*/

	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public CoreModel getResponseBody() {
		return responseBody;
	}


	public void setResponseBody(CoreModel responseBody) {
		this.responseBody = responseBody;
	}

	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
     
}
