package com.nadmat.dto;

import java.util.List;

public class MasterDataDTO {

	private int mappingId;
	private String cersaiPanNo;
	private String cersaiAddress;
	private String hAddress;
	private String state;
	private String district;
	private String taluka;
	private String pinCode;
	private String area;
	private String score;
	private int srNo;
	private int isValidated;
	private List<MasterDataDTO> listMasterData;
	
	
	public List<MasterDataDTO> getListMasterData() {
		return listMasterData;
	}
	public void setListMasterData(List<MasterDataDTO> listMasterData) {
		this.listMasterData = listMasterData;
	}
	public int getMappingId() {
		return mappingId;
	}
	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}
	public String getCersaiPanNo() {
		return cersaiPanNo;
	}
	public void setCersaiPanNo(String cersaiPanNo) {
		this.cersaiPanNo = cersaiPanNo;
	}
	public String getCersaiAddress() {
		return cersaiAddress;
	}
	public void setCersaiAddress(String cersaiAddress) {
		this.cersaiAddress = cersaiAddress;
	}
	public String gethAddress() {
		return hAddress;
	}
	public void sethAddress(String hAddress) {
		this.hAddress = hAddress;
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
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	
	public int getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	@Override
	public String toString() {
		return "MasterDataDTO [mappingId=" + mappingId + ", cersaiPanNo=" + cersaiPanNo + ", cersaiAddress="
				+ cersaiAddress + ", hAddress=" + hAddress + ", state=" + state + ", district=" + district + ", taluka="
				+ taluka + ", pinCode=" + pinCode + ", area=" + area + ", score=" + score + ", srNo=" + srNo
				+ ", isValidated=" + isValidated + ", listMasterData=" + listMasterData + "]";
	}
	
}
