package com.nadmat.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_error_log")
public class ErrorLogDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_err_id")
	private String appErrorId;
	@Column(name = "err_source")
	private String errSource;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "err_exception")
	private String errException;
	@Column(name = "stack_trace")
	private String stackTrace;
	@Column(name = "created_on")
	private String createdOn;
	@Column(name = "created_by")
	private String createdBy;

	public String getAppErrorId() {
		return appErrorId;
	}

	public void setAppErrorId(String appErrorId) {
		this.appErrorId = appErrorId;
	}

	public String getErrSource() {
		return errSource;
	}

	public void setErrSource(String errSource) {
		this.errSource = errSource;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getErrException() {
		return errException;
	}

	public void setErrException(String errException) {
		this.errException = errException;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
