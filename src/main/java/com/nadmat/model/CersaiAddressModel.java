package com.nadmat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "cersai_address_temp")
public class CersaiAddressModel {

	@Id
	@Column(name = "ca_id")
	private int caId;
	@Column(name = "pan_no")
	private String panNo;
	@Column(name = "cersai_address")
	private String cersaiAddress;
	@Column(name = "description_of_asset")
	private  String  descriptionOfAsset;
	@Column(name = "survery_municipal_no")
	private  String  surveryMunicipalNo;
	@Column(name = "plot_no")
	private  String  plotNo;
	@Column(name = "house_flat_unit_no")
	private  String  houseFlatUnitNo;
	@Column(name = "floor_no")
	private  String  floorNo;
	@Column(name = "area")
	private  String  area;
	@Column(name = "building_tower_name_no")
	private  String  buildingTowerNameNo;
	@Column(name = "name_of_project_scheme")
	private  String  nameOfProjectScheme;
	@Column(name = "street_name_no")
	private  String  streetNameNo;
	@Column(name = "pocket")
	private  String  pocket;
	@Column(name = "locality")
	private  String  locality;
	@Column(name = "state")
	private  String  state;
	@Column(name = "district")
	private  String  district;
	@Column(name = "city")
	private  String  city;
	@Column(name = "pincode")
	private  int  pincode;
	@Column(name = "coordinate_1")
	private  String  coordinate1;
	@Column(name = "coordinate_2")
	private  String  coordinate2;
	@Column(name = "coordinate_3")
	private  String  coordinate3;
	@Column(name = "coordinate_4")
	private  String  coordinate4;
	@Column(name = "master_village")
	private  String  masterVillage;
	@Column(name = "master_taluka")
	private  String  masterTaluka;
	@Column(name = "master_district")
	private  String  masterDistrict;
	@Column(name = "master_state")
	private  String  masterState;
	@Column(name = "master_pincode")
	private  int  masterPincode;
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
	
	public int getCaId() {
		return caId;
	}
	public void setCaId(int caId) {
		this.caId = caId;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getCersaiAddress() {
		return cersaiAddress;
	}
	public void setCersaiAddress(String cersaiAddress) {
		this.cersaiAddress = cersaiAddress;
	}
	public String getDescriptionOfAsset() {
		return descriptionOfAsset;
	}
	public void setDescriptionOfAsset(String descriptionOfAsset) {
		this.descriptionOfAsset = descriptionOfAsset;
	}
	public String getSurveryMunicipalNo() {
		return surveryMunicipalNo;
	}
	public void setSurveryMunicipalNo(String surveryMunicipalNo) {
		this.surveryMunicipalNo = surveryMunicipalNo;
	}
	public String getPlotNo() {
		return plotNo;
	}
	public void setPlotNo(String plotNo) {
		this.plotNo = plotNo;
	}
	public String getHouseFlatUnitNo() {
		return houseFlatUnitNo;
	}
	public void setHouseFlatUnitNo(String houseFlatUnitNo) {
		this.houseFlatUnitNo = houseFlatUnitNo;
	}
	public String getFloorNo() {
		return floorNo;
	}
	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBuildingTowerNameNo() {
		return buildingTowerNameNo;
	}
	public void setBuildingTowerNameNo(String buildingTowerNameNo) {
		this.buildingTowerNameNo = buildingTowerNameNo;
	}
	public String getNameOfProjectScheme() {
		return nameOfProjectScheme;
	}
	public void setNameOfProjectScheme(String nameOfProjectScheme) {
		this.nameOfProjectScheme = nameOfProjectScheme;
	}
	public String getStreetNameNo() {
		return streetNameNo;
	}
	public void setStreetNameNo(String streetNameNo) {
		this.streetNameNo = streetNameNo;
	}
	public String getPocket() {
		return pocket;
	}
	public void setPocket(String pocket) {
		this.pocket = pocket;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	public String getCoordinate1() {
		return coordinate1;
	}
	public void setCoordinate1(String coordinate1) {
		this.coordinate1 = coordinate1;
	}
	public String getCoordinate2() {
		return coordinate2;
	}
	public void setCoordinate2(String coordinate2) {
		this.coordinate2 = coordinate2;
	}
	public String getCoordinate3() {
		return coordinate3;
	}
	public void setCoordinate3(String coordinate3) {
		this.coordinate3 = coordinate3;
	}
	public String getCoordinate4() {
		return coordinate4;
	}
	public void setCoordinate4(String coordinate4) {
		this.coordinate4 = coordinate4;
	}
	public String getMasterVillage() {
		return masterVillage;
	}
	public void setMasterVillage(String masterVillage) {
		this.masterVillage = masterVillage;
	}
	public String getMasterTaluka() {
		return masterTaluka;
	}
	public void setMasterTaluka(String masterTaluka) {
		this.masterTaluka = masterTaluka;
	}
	public String getMasterDistrict() {
		return masterDistrict;
	}
	public void setMasterDistrict(String masterDistrict) {
		this.masterDistrict = masterDistrict;
	}
	public String getMasterState() {
		return masterState;
	}
	public void setMasterState(String masterState) {
		this.masterState = masterState;
	}
	public int getMasterPincode() {
		return masterPincode;
	}
	public void setMasterPincode(int masterPincode) {
		this.masterPincode = masterPincode;
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
