package com.example.backend.exceptions;

public class SocialMediaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SocialMediaException(String exceptionMessage , Exception exception) {
		super(exceptionMessage,exception);
	}
	
	public SocialMediaException(String exceptionMessage) {
		super(exceptionMessage);
	}
	
	

}
