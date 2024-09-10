package com.nadmat.model;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("UserDetails")
public class UserRedisModel implements Serializable {
	
	@Id
	private String panNo;
	private int runType;
	private int userId;
    private String userName;
    private String createdOn;
    
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public int getRunType() {
		return runType;
	}
	public void setRunType(int runType) {
		this.runType = runType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
