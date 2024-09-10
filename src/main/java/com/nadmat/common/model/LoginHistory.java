package com.nadmat.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login_history")
public class LoginHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_history_id")
	private int loginHistoryId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "ip_address")
	private String ipAddress;
	@Column(name = "login_date")
	private Date loginDate;
	@Column(name = "logout_date")
	private Date logoutDate;

	public int getLoginHistoryId() {
		return loginHistoryId;
	}

	public void setLoginHistoryId(int loginHistoryId) {
		this.loginHistoryId = loginHistoryId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	@Override
	public String toString() {
		return "LoginHistory [loginHistoryId=" + loginHistoryId + ", userName=" + userName + ", ipAddress=" + ipAddress
				+ ", loginDate=" + loginDate + ", logoutDate=" + logoutDate + "]";
	}

}
