package com.nadmat.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nadmat.common.Config;
import com.nadmat.dto.ProgressBarDTO;
import com.nadmat.dto.RequestDataDTO;
import com.nadmat.dto.SampleMasterDTO;
import com.nadmat.dto.SampleResultDTO;
import com.nadmat.model.RunMasterModel;
import com.nadmat.model.SampleMasterModel;
import com.nadmat.model.SampleResultModel;
import com.nadmat.model.UnprocessedSampleModel;
import com.nadmat.model.ValidatedSampleModel;
import com.nadmat.repo.LookupRepo;
import com.nadmat.repo.RunMasterRepo;
import com.nadmat.repo.SampleMasterRepo;
import com.nadmat.repo.SampleResultRepo;
import com.nadmat.repo.UnprocessedSampleRepo;
import com.nadmat.repo.ValidatedSampleRepo;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 
 * @author Vishal
 * @since 2022-05-03
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 05-05-22		Vishal		- Create getSAddress method for get all search string from sample_master
 * 05-05-22		Vishal		- Create callNadmatAPI method for get solr response and save on sample_result table on DB 
 * 06-05-22		Vishal		- Create getDistinctSearchString for get distinct search string from DB and set on search string field on UI
 * 06-05-22		Vishal		- Create getSampleMaster method for get sample master data from sample_result table on DB based on search string.
 * 06-05-22		Vishal		- Create saveAndDeleteValidatedSample method for Save and delete record from validated_sample table 
 * 11-05-22		Vishal		- Check if processStatus is 0 or 1, update processStatus in sample_master table
 * 12-05-22		Vishal		- Create getProgressbarSummay method for update progress bar based on toggle is on and off.
 * 16-05-22		Vishal		- Create getTotalCount and getSmDetailsBySearchStr method for get total number of record.
 * 24-05-22		Vishal		- Comment code of create highlighted address in callNadmatAPI method.
 * 25-05-22		Vishal		- Change JPA update method to native update sample_master method for modified_on is use system time instead of current_timestamp.
 * 25-05-22		Vishal		- Insert error log into app_error_log table in database 
 * 14-06-22		Vishal		- Update progressbar data based on sampleType
 * 22-06-22		Vishal		- Implement re-run functionality and run only unprocessed smId with same runId. 
 * 23-06-22		Vishal		- Add sampleType perameter in getSampleMaster method for show result table based on latest runId and sampleType.
 */


@Service
public class ValidationServiceImpl {

	@Autowired
	Environment environment;
	
	@Autowired
	RunMasterRepo runMasterRepo;
	
	@Autowired
	SampleResultRepo sampleResultRepo;
	
	@Autowired
	SampleMasterRepo sampleMasterRepo;
	
	@Autowired
	UnprocessedSampleRepo unprocessedSampleRepo;
	
	@Autowired
	ValidatedSampleRepo validatedSampleRepo;
	
	@Autowired
	LookupRepo lookupRepo;
	
	/**
	 * Get all search string from sample_master table and save the run master details and update status on run_master table.
	 * @param runType 
	 * @param runName 
	 * @param sampleType 
	 * @param userId 
	 * @return status
	 */
	public int getSAddress(int runType, String runName, int sampleType, String userId) {
		int status = 0;
		int runId = 0;
		int smId = 0;
		String coreName = null;
		if (runType == 5) {
			coreName = this.environment.getProperty("app.sample_data_core_name");
		}else if (runType == 6) {
			coreName = this.environment.getProperty("app.sample_name_data_core_name");
		}
		
		RunMasterModel masterModel = new RunMasterModel();
		List<Object[]> sampleMasterList = null;
		try {
			int defualtRunId = lookupRepo.getDefaultRunId();
			System.err.println(defualtRunId);
			if (defualtRunId == -1) {
				status = 1;
				masterModel.setCoreName(coreName);
				masterModel.setCreatedBy(Integer.valueOf(userId));
				masterModel.setRemark(runName);
				masterModel.setRunTypeId(runType);
				runMasterRepo.save(masterModel);
				runId = masterModel.getRunId();
				sampleMasterList = sampleMasterRepo.getsampleMasterBySampleType(sampleType);
			} else {
				status = 1;
				runId = defualtRunId;
				sampleMasterList = sampleMasterRepo.getUnprocessSampleMaster(runId, sampleType);
			}
			int counter = 0;
			Iterator<Object[]> sampleSummay = sampleMasterList.iterator();
			while (sampleSummay.hasNext()) {
				Object[] obj = sampleSummay.next();
				String searchString = obj[1].toString().replace(":", "");
				smId = Integer.valueOf(obj[0].toString());
				int mdId = Integer.valueOf(obj[2].toString());
				if (!searchString.equalsIgnoreCase("")) {
					status = callNadmatAPI(runType, smId, mdId, searchString, runId, coreName, userId);
				}
				counter++;
//				if (counter == 10) {
//					break;
//				}
			}
			
//			for (Iterator iterator = searchList.iterator(); iterator.hasNext();) {
//				SampleMasterModel searchMasterModel = (SampleMasterModel) iterator.next();
//				String searchString = searchMasterModel.getSearchString().replace(":", "");
//				System.out.println(searchString);
//				smId = searchMasterModel.getSmId();
//				int mdId = searchMasterModel.getMdId();
//				if (!searchString.equalsIgnoreCase("")) {
//					status = callNadmatAPI(runType, smId, mdId, searchString, runId, coreName, userId);
//				}
//				counter++;
////				if (counter == 10) {
////					break;
////				}
//			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		
		RunMasterModel updRunMaster = runMasterRepo.findByRunId(runId);
		updRunMaster.setStatus(status);
		updRunMaster.setModifiedBy(Integer.valueOf(userId));
		runMasterRepo.save(updRunMaster);
		
		return status;
	}

	/**
	 * Call NADMAT API for get solr response and save on sample_result table on DB
	 * 	- Give searchString, coreName, mdId as a input and set highlight text in search String.
	 * @param runType
	 * @param smId
	 * @param mdId
	 * @param searchString
	 * @param runId
	 * @param coreName
	 * @param userId
	 * @return status
	 */
	private int callNadmatAPI(int runType, int smId, int mdId, String searchString, int runId, String coreName, String userId) {
		int status = 1;
		List<SampleResultModel> apiResponseList = new ArrayList<SampleResultModel>();
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		String apiUrl = this.environment.getProperty("app.sample_data");
		try {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
		urlBuilder.addQueryParameter("searchString", searchString);
		urlBuilder.addQueryParameter("coreName", coreName);
		urlBuilder.addQueryParameter("mdId", String.valueOf(mdId));
		String url = urlBuilder.build().toString();

		Request request = new Request.Builder()
		                     .url(url)
		                     .method("GET", null)
		                     .build();
		
			Response response = client.newCall(request).execute();
			String jsonData = response.body().string();
			JSONObject jObj = new JSONObject(jsonData); 
			JSONArray masterList = (JSONArray) jObj.get("Data");
			 for (int i = 0; i < masterList.length(); i++) {
				 JSONObject jsonList = masterList.getJSONObject(i);
				 if (masterList.length() == 1 && jsonList.get("mdId").toString().equals("")) {
					 UnprocessedSampleModel addressModel = new UnprocessedSampleModel();
						addressModel.setApiUrl(jsonList.get("apiUrl").toString());
						addressModel.setRunId(runId);
						addressModel.setSmId(smId);
						addressModel.setCreatedBy(Integer.valueOf(userId));
						unprocessedSampleRepo.save(addressModel);
				} else {
					// set highlight text in search String
//					String hlString = "";
//					if (jsonList.get("hlAddress").toString() != "") {
//						hlString = jsonList.get("searchString").toString();
//						String hlAddress = jsonList.get("hlAddress").toString();
//						System.out.println("hlString before ==== "+hlString);
//						System.out.println("hlAddress before ==== "+hlAddress);
//						Document document = Jsoup.parse(hlAddress);
//						Elements paragraphs = document.getElementsByTag("em");
//						Set<String> distWord = new HashSet<String>();
//						hlAddress = hlAddress.replaceAll("<strong><em>", "");
//						hlAddress = hlAddress.replaceAll("</em></strong>", "");
//						for (Element hWord : paragraphs) {
//							distWord.add(hWord.text());
//						}
//						for (String distHWord : distWord) {
//							System.out.println("distHWord-- "+distHWord);
//							hlAddress = hlAddress.replaceAll("<strong><em>" + distHWord + "</em></strong>",distHWord);
////							if (hlString.toLowerCase().contains(distHWord.toLowerCase())) {
////								hlString = hlString.replaceAll("\\b"+distHWord+"\\b","<strong><em>" + distHWord + "</em></strong>");
////							}
//						}
//						if (hlAddress.equalsIgnoreCase(hlString)) {
//							System.out.println("if--- hlString-- "+hlString);
//						}else {
//							System.err.println("else--- hlString-- "+hlString);
//							System.err.println("else--- hlAddress-- "+hlAddress);
//							if (j == 10) {
//								break;
//							}
//							j++;
//						}
//					}
					SampleResultModel resultModel = new SampleResultModel();
					 resultModel.setRunId(runId);
					 resultModel.setSmId(smId);
					 resultModel.setMdId(jsonList.get("mdId").equals("") ? 0 : Integer.parseInt(jsonList.get("mdId").toString()));
					 resultModel.setScore(jsonList.get("score").toString());
					 resultModel.setApiResponse(jsonList.get("apiResponse").toString());
					 resultModel.setHighlightResponse(jsonList.get("hlAddress") == "" ? "" : jsonList.get("hlAddress").toString());
					 resultModel.setApiUrl(jsonList.get("apiUrl").toString());
					 resultModel.setCreatedBy(Integer.valueOf(userId));
					 apiResponseList.add(resultModel);
				}
				 
			 }
			 sampleResultRepo.saveAll(apiResponseList);
			 status = 2;
		} catch (Exception e) {
			status = 3;
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return status;
	}

	/**
	 * Get distinct search string from DB and set on search string field on UI.
	 * @param bottonType
	 * @param smId
	 * @param isDispValidated
	 * @param sampleType 
	 * @param userId
	 * @return searchStringList
	 */
	public List<SampleMasterModel> getDistinctSearchString(String bottonType, int smId, int isDispValidated, int sampleType, String userId) {
		List<SampleMasterModel> searchStringList = new ArrayList<SampleMasterModel>();
		List<Object[]> searchString = null;
		try {
			if (isDispValidated == 1) {
				if (bottonType.equalsIgnoreCase("next")) {
					searchString =  sampleMasterRepo.getFirstSampleMasterASC(smId, sampleType);
				}else if (bottonType.equalsIgnoreCase("prev")) {
					searchString =  sampleMasterRepo.getFirstSampleMasterDESC(smId, sampleType);
				}else if (bottonType.equalsIgnoreCase("onReload")) {
					searchString =  sampleMasterRepo.getFirstSampleMaster(smId, sampleType);
				}else {
					searchString =  sampleMasterRepo.getFirstSampleMasterASC(smId, sampleType);
				}
			} else {
				if (bottonType.equalsIgnoreCase("next")) {
					searchString =  sampleMasterRepo.getFirstByProcessStatusASC(Integer.valueOf(userId), sampleType);
				}else if (bottonType.equalsIgnoreCase("prev")) {
					searchString =  sampleMasterRepo.getFirstByProcessStatusDESC(Integer.valueOf(userId), sampleType);
				}else {
					searchString =  sampleMasterRepo.getFirstByProcessStatusASC(Integer.valueOf(userId), sampleType);
				}
			}
			Iterator<Object[]> sampleSummay = searchString.iterator();
			while (sampleSummay.hasNext()) {
				Object[] obj = sampleSummay.next();
				SampleMasterModel objResultModel = new SampleMasterModel();
				objResultModel.setSmId(Integer.valueOf(obj[0].toString()));
				objResultModel.setSearchString(obj[1].toString());
				objResultModel.setProcessStatus(Integer.valueOf(obj[2].toString()));
				searchStringList.add(objResultModel);
			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return searchStringList;
	}

	/**
	 * Get sample master data from sample_result table on DB based on search string.
	 * 		- check display validated string toggle is On, show validated search string data else invalidated search string record.
	 * 		- update processStatus in sample_master table.
	 * @param smId
	 * @param isDispValidated
	 * @param userId
	 * @param processStatus
	 * @param sampleType 
	 * @return sampleMasterList
	 */
	public List<SampleResultDTO> getSampleMaster(int smId, int isDispValidated, String userId, int processStatus, int sampleType) {
		int status = 1;
		List<SampleResultDTO> sampleMasterList = new ArrayList<SampleResultDTO>();
		try {
			if (processStatus == 2) {
				List<Object[]> sampleResultList = sampleResultRepo.getSmapleMasterBySmId(smId,sampleType);
				Iterator<Object[]> sampleSummay = sampleResultList.iterator();
				while (sampleSummay.hasNext()) {
					Object[] obj = sampleSummay.next();
					SampleResultDTO objResultModel = new SampleResultDTO();
					objResultModel.setMdId(Integer.valueOf(obj[2].toString()));
					objResultModel.setSearchString(obj[4].toString());
					objResultModel.setScore(obj[5].toString());
					objResultModel.setIsValidated(Integer.valueOf(obj[6].toString()));
					sampleMasterList.add(objResultModel);
				}
			} else {
				List<Object[]> sampleResult = sampleResultRepo.getSmapleMasterBySmIdAndPS(smId,sampleType);
				Iterator<Object[]> sampleSummay = sampleResult.iterator();
				while (sampleSummay.hasNext()) {
					Object[] obj = sampleSummay.next();
					SampleResultDTO objResultModel = new SampleResultDTO();
					objResultModel.setMdId(Integer.valueOf(obj[2].toString()));
					objResultModel.setSearchString(obj[4].toString());
					objResultModel.setScore(obj[5].toString());
					objResultModel.setIsValidated(Integer.valueOf(obj[6].toString()));
					sampleMasterList.add(objResultModel);
				}
			}
			// check if processStatus is 0 or 1, update processStatus in sample_master table
			if (processStatus != 2) {
				int modifiedBy =Integer.parseInt(userId);
				sampleMasterRepo.updateStatus(modifiedBy, status, smId);
			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return sampleMasterList;
	}

	/**
	 * Save and delete record from validated_sample table 
	 * 		- check validated record based on smId if found, delete all data based on smId and save, else only save the data.
	 * 		- update processStatus on sample_master table when processStatus is 0 or 1.
	 * @param requestData
	 * @param userId
	 * @return status
	 */
	@Transactional
	public int saveAndDeleteValidatedSample(RequestDataDTO requestData, String userId) {
		int status = 1;
		int smId = requestData.getSmId();
		List<RequestDataDTO> selectedDataList = requestData.getSelectedList();
		List<ValidatedSampleModel> validatedResultList = new ArrayList<ValidatedSampleModel>();
		try {
			status = 2;
			List<ValidatedSampleModel> validatedResult = validatedSampleRepo.findBySmId(smId);
			if (validatedResult.size()>0) {
				validatedSampleRepo.deleteBySmId(smId);
			} 
			for (RequestDataDTO requestDataDTO : selectedDataList) {
				ValidatedSampleModel  resultModel = new ValidatedSampleModel();
				resultModel.setSmId(smId);
				resultModel.setMdId(requestDataDTO.getMdId());
				resultModel.setIsActive(1);
				resultModel.setCreatedBy(Integer.valueOf(userId));
				validatedResultList.add(resultModel);
			}
			validatedSampleRepo.saveAll(validatedResultList);
			
			int processStatus = status;
			int modifiedBy =Integer.parseInt(userId);
			sampleMasterRepo.updateStatus(modifiedBy, processStatus, smId);

		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return status;
	}

	/**
	 * Update progress bar based on toggle is on and off.
	 * @param smId
	 * @param isDispValidated
	 * @param sampleType 
	 * @param userId
	 * @return progressBarDTO
	 */
	public ProgressBarDTO getProgessBarSummary(int smId, int isDispValidated, int sampleType, String userId) {
		int	rowNum = 0;
		int validatedCount = 0;
		ProgressBarDTO progressBarDTO = new ProgressBarDTO();
		try {
			if (isDispValidated == 1) {
				rowNum =  sampleMasterRepo.getProgressBarByDispValidated(smId, sampleType); 
			}else {
				validatedCount = sampleMasterRepo.getProgressBarByNotDispValidated(sampleType);
			}
			progressBarDTO.setIndexOfStr(rowNum);
			progressBarDTO.setValidatedStr(validatedCount);
			
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return progressBarDTO;
	}

	/**
	 * Get sample master details from sample_master table based on search string.
	 * @param searchStr
	 * @param sampleType
	 * @param userId 
	 * @return sampleMasterList
	 */
	public List<SampleMasterDTO> getSmDetailsBySearchStr(String searchStr, int sampleType, String userId) {
		List<SampleMasterDTO> sampleMasterList = new ArrayList<SampleMasterDTO>();
		try {
			List<Object[]> sampleResultList = sampleMasterRepo.getsmIdBySearchStr(searchStr, sampleType);
			Iterator<Object[]> sampleSummay = sampleResultList.iterator();
			while (sampleSummay.hasNext()) {
				Object[] obj = sampleSummay.next();
				SampleMasterDTO sampleMasterDTO = new SampleMasterDTO();
				sampleMasterDTO.setSmId(Integer.parseInt(obj[0].toString()));
				sampleMasterDTO.setProcessStatus(Integer.parseInt(obj[1].toString()));
				sampleMasterList.add(sampleMasterDTO);
			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
			
		return sampleMasterList;
	}
	
	/**
	 * Get total number of record from database.
	 * @param sampleType 
	 * @return
	 */
	public int getTotalCount(int sampleType) {
		int totalAddress = sampleMasterRepo.getTotalSearchStr(sampleType); 
		return totalAddress;
	}


}
