package com.ecomerce.sistema.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void setList(List<FieldMessage> errors) {
		this.errors = errors;
	}
	public void addError(String fildName, String message) {		
		this.errors.add(new FieldMessage(fildName, message));
	}
}
