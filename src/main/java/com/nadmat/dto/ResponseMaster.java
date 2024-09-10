package com.nadmat.dto;


public class ResponseMaster {

	private int Status;
	private Boolean Result;
	private String Message;
	private Object Data;

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
	
	public Boolean getResult() {
		return Result;
	}

	public void setResult(Boolean result) {
		Result = result;
	}

	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	
}
