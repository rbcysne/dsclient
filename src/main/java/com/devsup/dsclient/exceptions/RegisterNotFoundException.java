package com.devsup.dsclient.exceptions;

public class RegisterNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegisterNotFoundException() {
		
	}
	
	public RegisterNotFoundException(String msg) {
		super(msg);
	}
}
