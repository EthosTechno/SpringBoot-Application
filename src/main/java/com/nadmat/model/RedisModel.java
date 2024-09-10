package com.nadmat.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("PanDetails")
public class RedisModel implements Serializable {

	@Id
	private String panNo;
	private int saId;
    private String address;
    private int mappingId;
    private String cersaiPanNo;
    private String cersaiAddress;
    private String area;
    private String district;
    private String taluka;
    private String state;
    private String apiResponse;
    private String highlightedResponse;
    private int pincode;
    private String score;
    private int isValidated;
    private int runType;
    private List<RedisModel> panDetailsList;
    
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public int getSaId() {
		return saId;
	}
	public void setSaId(int saId) {
		this.saId = saId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getApiResponse() {
		return apiResponse;
	}
	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}
	public String getHighlightedResponse() {
		return highlightedResponse;
	}
	public void setHighlightedResponse(String highlightedResponse) {
		this.highlightedResponse = highlightedResponse;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public int getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	public int getRunType() {
		return runType;
	}
	public void setRunType(int runType) {
		this.runType = runType;
	}
	public List<RedisModel> getPanDetailsList() {
		return panDetailsList;
	}
	public void setPanDetailsList(List<RedisModel> panDetailsList) {
		this.panDetailsList = panDetailsList;
	}
    
}
