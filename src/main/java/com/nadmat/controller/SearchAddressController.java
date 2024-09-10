package com.nadmat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nadmat.common.Config;
import com.nadmat.dto.MasterDataDTO;
import com.nadmat.dto.ResponseMaster;
import com.nadmat.dto.ResultMasterDTO;
import com.nadmat.model.LookupModel;
import com.nadmat.model.RunMasterModel;
import com.nadmat.service.SearchAddressServiceImpl;

/**
 * This class is navigate to relevant method in SearchAddressServiceImpl class.
 * 
 * @author Vishal
 * @since 2022-02-15
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     Author    Comment
 * ---------------------------------------------------------------------------
 * 18-02-22		Vishal		- Create getPanNumber and getRunId method for populate dropdown from UI
 * 21-02-22		Vishal		- Create getSearchAddress for populate search address based on selected pan number from UI
 * 24-02-22		Vishal		- Create getMasterAddress for populate master address based on selected pan number and search address from UI
 * 25-02-22		Vishal		- Create clearRedisByPan for clear redis data based on pan number 
 * 09-03-22		Vishal		- Created validatedAddress for validate search addresses with master data and update on Redis data with validated flag
 * 11-03-22		Vishal		- Created insertUpdatedPan method for update and save the pan details into database
 * 29-03-22		Vishal		- Get userId from session and set as input parameter in some methods.
 * 31-03-22		Vishal		- Create runType method for Get runType from Db.
 * 20-04-22		Vishal		- Create cersaiAddressSearch method for Get cersai address,id, and state from cersai_address_temp table 
 * 25-05-22		Vishal		- Insert error log into app_error_log table in database
 */

@RestController
public class SearchAddressController {
	@Autowired
	SearchAddressServiceImpl searchAddressServiceImpl;
	
	/**
	 * Copy solr config files from solr directory to other destination.
	 * @param coreName
	 * @param runId
	 */
	@RequestMapping(value = "/copyFiles", method = RequestMethod.GET)
	public void copyFiles(HttpServletRequest request, @QueryParam("coreName") String coreName, @QueryParam("runId") int runId) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		searchAddressServiceImpl.copyConfFiles(coreName, runId, userId);
	}
	
	/**
	 * Insert query parameter into run_master table and call solr API
	 * @param request
	 * @param runName
	 * @param runType
	 * @return
	 */
	@RequestMapping(value = "/addressSearch", method = RequestMethod.GET)
	public int addressSearch(HttpServletRequest request, @RequestParam(value="runName") String runName, @RequestParam(value="runType") int runType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return searchAddressServiceImpl.getSAddress(runType, runName,userId);
	}
	
	/**
	 * Get all distinct pan number from DB
	 * @return
	 */
	@RequestMapping(value = "/getPanNumber", method = RequestMethod.GET)
	public List<String> getPanNumber() {
		return searchAddressServiceImpl.getPanNumber();
	}
	/**
	 * Get Run Id from run_master table from DB
	 * @param runType
	 * @return
	 */
	@RequestMapping(value = "/getRunId", method = RequestMethod.GET)
	public List<RunMasterModel> getRunId(@RequestParam(value="runType") int runType) {
		return searchAddressServiceImpl.getRunId(runType);
	}
	
	/**
	 * Get RunType from lookup table from DB
	 * @return
	 */
	@RequestMapping(value = "/getRunType", method = RequestMethod.GET)
	public List<LookupModel> getRunType() {
		return searchAddressServiceImpl.getRunType();
	}
	
	/**
	 * Get all search address based on selected pan number and runId from DB
	 * @param runId
	 * @param panNo
	 * @param userName 
	 * @param userId
	 * @param runType
	 * @return
	 */
	@RequestMapping(value = "/getSearchAddress", method = RequestMethod.GET)
	public ResultMasterDTO getSearchAddress(HttpServletRequest request, @RequestParam(value="runId") int runId, 
			@RequestParam(value="panNo") String panNo, @RequestParam(value="runType") int runType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		String userName = String.valueOf(session.getAttribute("user_name"));
		return searchAddressServiceImpl.checkPanIsExistInRedis(panNo, runId, userId, userName, runType);
	}
	
	/**
	 * Get all master address based on selected pan number and saId from Redis server 
	 * @param panNo
	 * @param saId
	 * @return responseMaster
	 */
	@RequestMapping(value = "/getMasterAddress", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON)
	public String getMasterAddress(HttpServletRequest request, @RequestParam(value="panNo") String panNo, @RequestParam(value="saId") int saId) {
		ResponseMaster response = new ResponseMaster();
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		try {
			List<MasterDataDTO> masterDataDTO = searchAddressServiceImpl.getMasterAddress(panNo, saId,userId);
			response.setData(masterDataDTO);
			response.setResult(true);
			response.setStatus(1);
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return new Gson().toJson(response);
	}
	
	/**
	 * Validate master data based on serach addresses and update on redis data with validated flag
	 * @param panNo
	 * @param saId
	 * @param amId
	 * @param isValid
	 * @return
	 */
	@RequestMapping(value = "/validatedAddress", method = RequestMethod.GET)
	public int validatedAddress(HttpServletRequest request, @RequestParam(value="panNo") String panNo, @RequestParam(value="saId") int saId, 
			@RequestParam(value="mappingId") int mappingId, @RequestParam(value="isValid") int isValid) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return searchAddressServiceImpl.validatedAddress(panNo, saId, mappingId, isValid, userId);
	}
	
	/**
	 * Update and save the pan details into database
	 * @param panNo
	 * @param runType
	 * @param request
	 */
	@RequestMapping(value = "/insertUpdatedPan", method = RequestMethod.GET)
	public void insertUpdatedPan(HttpServletRequest request, @RequestParam(value="panNo") String panNo, @RequestParam(value="runType") int runType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		searchAddressServiceImpl.insertUpdatedPan(panNo, userId, runType);
	}
	/**
	 * Clear the data from redis based on Pan num.
	 * @param panNo
	 * @param request
	 */
	@RequestMapping(value = "/clearRedisByPan", method = RequestMethod.GET)
	public void clearRedisByPan(HttpServletRequest request, @RequestParam(value="panNo") String panNo) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		searchAddressServiceImpl.clearRedisByPan(panNo, userId);
	}
	
	/**
	 * Get cersai address,id, and state from cersai_address_temp table 
	 * @param request
	 * @param runName
	 * @param runType
	 * @return
	 */
	@RequestMapping(value = "/cersaiAddressSearch", method = RequestMethod.GET)
	public int cersaiAddressSearch(HttpServletRequest request, @RequestParam(value="runName") String runName, @RequestParam(value="runType") int runType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return searchAddressServiceImpl.getCersaiAddress(runType, runName, userId);
	}
	 
}
