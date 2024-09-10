package com.nadmat.dto;

public class ResultMasterDTO {

	private int status;
	private Boolean isPanExist;
	private String userName;
	private Object searchAddress;	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Boolean getIsPanExist() {
		return isPanExist;
	}

	public void setIsPanExist(Boolean isPanExist) {
		this.isPanExist = isPanExist;
	}

	public Object getSearchAddress() {
		return searchAddress;
	}

	public void setSearchAddress(Object searchAddress) {
		this.searchAddress = searchAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "ResultMasterDTO [status=" + status + ", isPanExist=" + isPanExist + ", userName=" + userName
				+ ", searchAddress=" + searchAddress + "]";
	}

}
