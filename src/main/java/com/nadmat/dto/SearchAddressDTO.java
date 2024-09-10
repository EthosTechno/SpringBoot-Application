package com.nadmat.dto;

public class SearchAddressDTO {
	
	private int srNo;
	private int saId;
	private String panNo;
	private String address;
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public int getSaId() {
		return saId;
	}
	public void setSaId(int saId) {
		this.saId = saId;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "SearchAddressDTO [srNo=" + srNo + ", saId=" + saId + ", panNo=" + panNo + ", address=" + address + "]";
	}
	
	

}
