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
@Table(name = "validated_search_address")
public class ValidatedSAModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vsa_id")
	private int vsaId;
	@Column(name = "sa_id")
	private int saId;
	@Column(name = "mapping_id")
	private int mappingId;
	@Column(name = "run_type_id ")
	private int runTypeId;
	@Column(name = "is_active")
	private int isActive;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@Column(name = "modified_on", nullable = true, insertable = false)
	private String modifiedOn;
	
	public int getVsaId() {
		return vsaId;
	}
	public void setVsaId(int vsaId) {
		this.vsaId = vsaId;
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
	public int getRunTypeId() {
		return runTypeId;
	}
	public void setRunTypeId(int runTypeId) {
		this.runTypeId = runTypeId;
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
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}
