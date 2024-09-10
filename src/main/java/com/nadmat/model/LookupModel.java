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
@Table(name = "lookup")
public class LookupModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lookup_id")
	private int lookupId;
	@Column(name = "lookup_type_id")
	private int lookupTypeId;
	@Column(name = "value")
	private String value;
	@Column(name = "code")
	private String code;
	@Column(name = "is_active")
	private int isActive;
	@Column(name = "created_by")
	private int createdBy;
	@Column(name = "created_on", nullable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "modified_by", nullable = true)
	private Integer modifiedBy;
	@Column(name = "modified_on")
	private Date modifiedOn;
	public int getLookupId() {
		return lookupId;
	}
	public void setLookupId(int lookupId) {
		this.lookupId = lookupId;
	}
	public int getLookupTypeId() {
		return lookupTypeId;
	}
	public void setLookupTypeId(int lookupTypeId) {
		this.lookupTypeId = lookupTypeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
