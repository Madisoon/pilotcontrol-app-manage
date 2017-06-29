package com.fantasi.common.db.service;

public class TransactionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919079571045171389L;
	
	public TransactionException(String msg, Throwable cause) {
		super(msg + (cause==null ? null : cause.toString()));
	}
	
	public String getErrorMessage() {
		return this.getMessage();
	}

}
