package com.nadmat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "sample_result")
public class SampleResultModel {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sr_id")
	private int srId;
	@Column(name = "run_id")
	private int runId;
	@Column(name = "sm_id")
	private int smId;
	@Column(name = "md_id")
	private int mdId;
	@Column(name = "score")
	private String score;
	@Column(name = "api_response")
	private String apiResponse;
	@Column(name = "highlight_response")
	private String highlightResponse;
	@Column(name = "api_url")
	private String apiUrl;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@Column(name = "modified_on")
	private Date modifiedOn;
	
	public int getSrId() {
		return srId;
	}
	public void setSrId(int srId) {
		this.srId = srId;
	}
	public int getRunId() {
		return runId;
	}
	public void setRunId(int runId) {
		this.runId = runId;
	}
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
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getApiResponse() {
		return apiResponse;
	}
	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}
	public String getHighlightResponse() {
		return highlightResponse;
	}
	public void setHighlightResponse(String highlightResponse) {
		this.highlightResponse = highlightResponse;
	}
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
}
