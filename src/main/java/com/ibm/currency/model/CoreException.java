package com.ibm.currency.model;

public class CoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode;

	public CoreException() {
		super();
	}
	
	
	public CoreException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    } 
	

}
