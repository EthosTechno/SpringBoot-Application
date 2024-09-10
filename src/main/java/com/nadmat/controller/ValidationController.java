package com.nadmat.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nadmat.dto.ProgressBarDTO;
import com.nadmat.dto.RequestDataDTO;
import com.nadmat.dto.SampleMasterDTO;
import com.nadmat.dto.SampleResultDTO;
import com.nadmat.model.LookupModel;
import com.nadmat.model.SampleMasterModel;
import com.nadmat.service.ValidationServiceImpl;

/**
 * This class is navigate to relevant method in ValidationServiceImpl class.
 * 
 * @author Vishal
 * @since 2022-05-03
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author   	Comment
 * ---------------------------------------------------------------------------
 * 05-05-22		Vishal		- Create addressSearch, getDistinctSearchString method for get search string from Db
 * 06-05-22		Vishal		- Create getSampleMaster, saveAndDelete method for populate data table on UI and save the validated data on DB.
 * 12-05-22		Vishal		- Create getProgressbarSummay method for update progress bar based on toggle is on and off.
 * 25-05-22		Vishal		- Insert error log into app_error_log table in database
 * 14-06-22		Vishal		- Give dynamic sampleType and based on sampleType update the data.
 * 23-06-22		Vishal		- Add sampleType Perameter in getSampleMaster method for show result table based on sampleType.
 */

@RestController
public class ValidationController {

	@Autowired
	ValidationServiceImpl validationServiceImpl;
	
	/**
	 * Insert query parameter into run_master table and call solr API
	 * @param request
	 * @param runName
	 * @param runType
	 * @return
	 */
	@RequestMapping(value = "/getSearchString", method = RequestMethod.GET)
	public int addressSearch(HttpServletRequest request, @RequestParam(value="runName") String runName, 
			@RequestParam(value="runType") int runType) {
		//, @RequestParam(value="sampleType") int sampleType
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		int sampleType = 0;
		if (runType == 5) {
			sampleType = 1;
		}else if (runType == 6) {
			sampleType = 2;
		}
		return validationServiceImpl.getSAddress(runType, runName, sampleType, userId);
	}
	

	/**
	 * Get all distinct search string from DB
	 * @param request
	 * @param bottonType
	 * @param smId
	 * @param isDispValidated
	 * @param sampleType
	 * @return
	 */
	@RequestMapping(value = "/getDistinctSearchString", method = RequestMethod.GET)
	public List<SampleMasterModel> getDistinctSearchString(HttpServletRequest request, @RequestParam(value="bottonType") String bottonType, 
			@RequestParam(value="smId") int smId, @RequestParam(value="isDispValidated") int isDispValidated, @RequestParam(value="sampleType") int sampleType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return validationServiceImpl.getDistinctSearchString(bottonType, smId, isDispValidated, sampleType, userId);
	}
	

	/**
	 * Get sample result data from sample_result table in DB based on smId.
	 * @param request
	 * @param smId
	 * @param sampleType
	 * @param isDispValidated
	 * @param processStatus
	 * @return
	 */
	@RequestMapping(value = "/getSampleMaster", method = RequestMethod.GET)
	public List<SampleResultDTO> getSampleMaster(HttpServletRequest request, @RequestParam(value="smId") int smId, @RequestParam(value="sampleType") int sampleType, 
			@RequestParam(value="isDispValidated") int isDispValidated, @RequestParam(value="processStatus") int processStatus) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return validationServiceImpl.getSampleMaster(smId, isDispValidated, userId, processStatus,sampleType);
	}
	
	/**
	 * Save and Delete sample result data from validated_sample table in DB based on smId.
	 * @param request
	 * @param requestData
	 * @return
	 */
	@RequestMapping(value = "/saveAndDelete", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON)
	public int saveAndDeleteValidatedSample(HttpServletRequest request, @RequestBody RequestDataDTO requestData) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		int status = validationServiceImpl.saveAndDeleteValidatedSample(requestData, userId);
		return status;
	}
	
	/**
	 * Update progress bar value from database
	 * @param request
	 * @param smId
	 * @param isDispValidated
	 * @param sampleType
	 * @return
	 */
	@RequestMapping(value = "/getProgessBarSummary", method = RequestMethod.GET)
	public ProgressBarDTO getProgessBarSummary(HttpServletRequest request, @RequestParam(value="smId") int smId,
			@RequestParam(value="isDispValidated") int isDispValidated, @RequestParam(value="sampleType") int sampleType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return validationServiceImpl.getProgessBarSummary(smId, isDispValidated, sampleType, userId);
	}
	
	/**
	 * Get total count of sample master record from database
	 * @param sampleType
	 * @return
	 */
	@RequestMapping(value = "/getTotalCount", method = RequestMethod.GET)
	public int getTotalCount(@RequestParam(value="sampleType") int sampleType) {
		return validationServiceImpl.getTotalCount(sampleType);
	}
	
	/**
	 * Update progress bar value from database
	 * @param request
	 * @param searchStr
	 * @param sampleType
	 * @return
	 */
	@RequestMapping(value = "/getSmDetailsBySearchStr", method = RequestMethod.GET)
	public List<SampleMasterDTO> getSmDetailsBySearchStr(HttpServletRequest request,@RequestParam(value="searchStr") String searchStr, 
			@RequestParam(value="sampleType") int sampleType) {
		HttpSession session = request.getSession();
		String userId = String.valueOf(session.getAttribute("user_id"));
		return validationServiceImpl.getSmDetailsBySearchStr(searchStr, sampleType,userId);
	}
	
}
