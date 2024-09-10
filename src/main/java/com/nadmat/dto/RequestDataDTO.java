package com.nadmat.dto;

import java.util.List;

public class RequestDataDTO {
	private int smId;
	private int mdId;
	private int isDispValidated;
	private int processStatus;
	private List<RequestDataDTO> selectedList;
	
	public int getSmId() {
		return smId;
	}
	public void setSmId(int smId) {
		this.smId = smId;
	}
	public int getMdId() {
		return mdId;
	}
	public void setMdId(int mdId) {
		this.mdId = mdId;
	}
	public int getIsDispValidated() {
		return isDispValidated;
	}
	public void setIsDispValidated(int isDispValidated) {
		this.isDispValidated = isDispValidated;
	}
	public List<RequestDataDTO> getSelectedList() {
		return selectedList;
	}
	public void setSelectedList(List<RequestDataDTO> selectedList) {
		this.selectedList = selectedList;
	}
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	@Override
	public String toString() {
		return "RequestDataDTO [smId=" + smId + ", mdId=" + mdId + ", isDispValidated=" + isDispValidated
				+ ", processStatus=" + processStatus + ", selectedList=" + selectedList + "]";
	}

	
}
