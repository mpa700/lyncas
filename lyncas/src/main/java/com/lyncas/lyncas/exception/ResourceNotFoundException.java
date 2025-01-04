package com.lyncas.lyncas.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 8801146422764530582L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
