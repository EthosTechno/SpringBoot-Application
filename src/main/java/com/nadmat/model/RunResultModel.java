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
@Table(name = "run_result")
public class RunResultModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rr_id")
	private int rrId;
	@Column(name = "run_id")
	private int runId;
	@Column(name = "sa_id")
	private int saId;
	@Column(name = "mapping_id")
	private int mappingId;
	@Column(name = "score")
	private String score;
	@Column(name = "api_response")
	private String apiResponse;
	@Column(name = "highlight_response")
	private String highlightResponse;
	@Column(name = "highlighted_cersai")
	private String highlightedCersai;
	@Column(name = "api_url")
	private String apiUrl;
	@Column(name = "mapped_am_id")
	private int mappedAmId;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@Column(name = "modified_on")
	private Date modifiedOn;
	public int getRrId() {
		return rrId;
	}
	public void setRrId(int rrId) {
		this.rrId = rrId;
	}
	public int getRunId() {
		return runId;
	}
	public void setRunId(int runId) {
		this.runId = runId;
	}
	public int getSaId() {
		return saId;
	}
	public void setSaId(int saId) {
		this.saId = saId;
	}
	public int getMappingId() {
		return mappingId;
	}
	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}
	public int getMappedAmId() {
		return mappedAmId;
	}
	public void setMappedAmId(int mappedAmId) {
		this.mappedAmId = mappedAmId;
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
	public String getHighlightedCersai() {
		return highlightedCersai;
	}
	public void setHighlightedCersai(String highlightedCersai) {
		this.highlightedCersai = highlightedCersai;
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
