package com.nadmat.dto;

public class ProgressBarDTO {
	private int totalSearchStr;
	private int validatedStr;
	private int indexOfStr;
	
	public int getTotalSearchStr() {
		return totalSearchStr;
	}
	public void setTotalSearchStr(int totalSearchStr) {
		this.totalSearchStr = totalSearchStr;
	}
	public int getValidatedStr() {
		return validatedStr;
	}
	public void setValidatedStr(int validatedStr) {
		this.validatedStr = validatedStr;
	}
	public int getIndexOfStr() {
		return indexOfStr;
	}
	public void setIndexOfStr(int indexOfStr) {
		this.indexOfStr = indexOfStr;
	}
	@Override
	public String toString() {
		return "ProgressBarDTO [totalSearchStr=" + totalSearchStr + ", validatedStr=" + validatedStr + ", indexOfStr="
				+ indexOfStr + "]";
	}
}
