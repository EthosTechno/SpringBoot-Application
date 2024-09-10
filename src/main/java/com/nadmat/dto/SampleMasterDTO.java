package com.nadmat.dto;

public class SampleMasterDTO {

	private  int smId;
	private String searchStr;
	private int processStatus;
	
	public int getSmId() {
		return smId;
	}
	public void setSmId(int smId) {
		this.smId = smId;
	}
	public String getSearchStr() {
		return searchStr;
	}
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	@Override
	public String toString() {
		return "SampleMasterDTO [smId=" + smId + ", searchStr=" + searchStr + ", processStatus=" + processStatus + "]";
	}
	
}
