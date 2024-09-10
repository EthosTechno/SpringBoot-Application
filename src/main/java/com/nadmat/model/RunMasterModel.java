package com.nadmat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "run_master")
public class RunMasterModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "run_id")
	private int runId;
	@Column(name = "run_type_id ")
	private int runTypeId;
	@Column(name = "status")
	private int status;
	@Column(name = "remark")
	private String remark;
	@Column(name = "core_name")
	private String coreName;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@Column(name = "modified_on", nullable = true, insertable = false)
	@UpdateTimestamp
	private Date modifiedOn;
	
	public int getRunId() {
		return runId;
	}
	public void setRunId(int runId) {
		this.runId = runId;
	}
	public int getRunTypeId() {
		return runTypeId;
	}
	public void setRunTypeId(int runTypeId) {
		this.runTypeId = runTypeId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCoreName() {
		return coreName;
	}
	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
