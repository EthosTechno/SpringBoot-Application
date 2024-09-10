package com.nadmat.dto;

public class SampleResultDTO {

	private int srNo;
	private int mdId;
	private String searchString;
	private String state;
	private String district;
	private String taluka;
	private String pinCode;
	private String area;
	private String score;
	private String hlAddress;
	private int isValidated;
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public int getMdId() {
		return mdId;
	}
	public void setMdId(int mdId) {
		this.mdId = mdId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTaluka() {
		return taluka;
	}
	public void setTaluka(String taluka) {
		this.taluka = taluka;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getHlAddress() {
		return hlAddress;
	}
	public void setHlAddress(String hlAddress) {
		this.hlAddress = hlAddress;
	}
	public int getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	@Override
	public String toString() {
		return "SampleResultDTO [srNo=" + srNo + ", mdId=" + mdId + ", searchString=" + searchString + ", state="
				+ state + ", district=" + district + ", taluka=" + taluka + ", pinCode=" + pinCode + ", area=" + area
				+ ", score=" + score + ", hlAddress=" + hlAddress + ", isValidated=" + isValidated + "]";
	}
	
	
}
