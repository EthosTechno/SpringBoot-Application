package com.nadmat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "sample_master")
public class SampleMasterModel {

	@Id
	@Column(name = "sm_id")
	private int smId;
	@Column(name = "search_string")
	private String searchString;
	@Column(name = "sample_type")
	private int sampleType;
	@Column(name = "process_status")
	private int processStatus;
	@Column(name = "md_id")
	private int mdId;
	@Column(name = "is_active")
	private int isActive;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@UpdateTimestamp
	@Column(name = "modified_on", nullable = true, insertable = false)
	private Date modifiedOn;
	
	public int getSmId() {
		return smId;
	}
	public void setSmId(int smId) {
		this.smId = smId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public int getSampleType() {
		return sampleType;
	}
	public void setSampleType(int sampleType) {
		this.sampleType = sampleType;
	}
	public int getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(int processStatus) {
		this.processStatus = processStatus;
	}
	public int getMdId() {
		return mdId;
	}
	public void setMdId(int mdId) {
		this.mdId = mdId;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
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
