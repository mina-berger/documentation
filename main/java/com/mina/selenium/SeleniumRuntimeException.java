package com.mina.selenium;

public class SeleniumRuntimeException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 2718988333950651692L;
	SeleniumRuntimeException(){
		super();
	}
	SeleniumRuntimeException(String message){
		super(message);
	}
	SeleniumRuntimeException(String message, Throwable cause){
		super(message, cause);
	}
	SeleniumRuntimeException(Throwable cause){
		super(cause);
	}
}
