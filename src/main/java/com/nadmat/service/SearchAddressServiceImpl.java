package com.nadmat.service;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nadmat.common.Config;
import com.nadmat.dto.MasterDataDTO;
import com.nadmat.dto.ResultMasterDTO;
import com.nadmat.dto.SearchAddressDTO;
import com.nadmat.model.CersaiAddressModel;
import com.nadmat.model.LookupModel;
import com.nadmat.model.RedisModel;
import com.nadmat.model.RunMasterModel;
import com.nadmat.model.RunResultModel;
import com.nadmat.model.SearchAddressModel;
import com.nadmat.model.UnprocessedAddressModel;
import com.nadmat.model.UserRedisModel;
import com.nadmat.model.ValidatedSAModel;
import com.nadmat.repo.CersaiAddressRepo;
import com.nadmat.repo.LookupRepo;
import com.nadmat.repo.RedisRepo;
import com.nadmat.repo.RunMasterRepo;
import com.nadmat.repo.RunResultRepo;
import com.nadmat.repo.SearchAddressRepo;
import com.nadmat.repo.UnprocessedAddressRepo;
import com.nadmat.repo.UserRedisRepo;
import com.nadmat.repo.ValidatedSARepo;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author Vishal
 * @since 2022-02-15
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 18-02-22		Vishal		- Create getPanNumber and getRunId method for populate dropdown from UI
 * 21-02-22		Vishal		- Create getSearchAddress for populate search address based on selected pan number from UI
 * 24-02-22		Vishal		- Create getMasterAddress for populate master address based on selected pan number and search address from UI
 * 25-02-22		Vishal		- Create clearRedisByPan for clear redis data based on pan number 
 * 09-03-22		Vishal		- Created validatedAddress for validate search addresses with master data and update on Redis data with validated flag
 * 11-03-22		Vishal		- Created insertUpdatedPan method for update and save the pan details into database
 * 15-03-22		Vishal		- Created getRunMaster and callNadmatAPI for get solr response from NADMATApi.
 * 17-03-22		Vishal		- Insert SolrAPI response into run_result table.
 * 21-03-22		Vishal		- Read core_name and api_url from application.properties in getAddresses and callNadmatAPI methods.
 * 22-03-22		Vishal		- Handle unprocessed address and insert that record into unprocessed_address table.
 * 28-03-22		Vishal		- Add user_id and pan in redis. Remove user current PAN detail on NEXT/PREV click and on change on PAN and insert new PAN
 * 29-03-22		Vishal		- Create checkPanIsExistInRedis method for check the pan number is exist or not in user redis data if yes, get the data from redis based on pan number 
 *								else get searchAddress from DB after the insert into redis.
 * 29-03-22		Vishal		- Apply lock on PAN for the user_id that another user is working on the same PAN and load data in read-only mode (from redis) for the PAN on UI
 * 30-03-22		Vishal		- Release lock from redis if lock is 20mins older and insert new record into the redis.
 * 31-03-22		Vishal		- Create runType method for get runtype from Db and give runType as a parameter for get data based on runType.
 * 13-04-22		Vishal		- Remove runId from input parameter in callNadamtApi method and remove highlight_response column from UI
 * 14-04-22 	Vishal		- Merge highlighted data and cersai data and insert into run_result table and get highlighted_cersai data and show on UI.
 * 19-04-22		Vishal		- Lock the Pan details with runType in checkPanIsExistInRedis method and Handle runType in insertUpdatedPan method 
 * 								and insert and update pan details in database based on runType.
 * 20-04-22		Vishal		- Create callNadmatAPIForCersai and getCersaiAddress method for cersai with master API 
 * 								- Call NadmatAPI for cersai with master and update 4 columns of Village, Taluka, Dist, State in cersai_address_temp table from area master after matching.
 * 29-04-22		Vishal		- Insert current pan into database and after clear redis data if pan summary is 20 min older in redis.
 * 25-05-22		Vishal		- Insert error log into app_error_log table in database
 */

@Service
public class SearchAddressServiceImpl {
	
	@Autowired
	LookupRepo lookupRepo;
	
	@Autowired
	RunMasterRepo runMasterRepo;
	
	@Autowired
	RunResultRepo runResultRepo;
	
	@Autowired
	SearchAddressRepo searchAddressRepo;
	
	@Autowired
	RedisRepo redisRepo;
	
	@Autowired
	ValidatedSARepo validatedSARepo;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	UnprocessedAddressRepo unprocessedAddressRepo;
	
	@Autowired
	UserRedisRepo userRedisRepo;
	
	@Autowired
	CersaiAddressRepo cersaiAddressRepo; 

	/**
	 * Copied solr config files from solr directory to other destination based on lookup type.
	 * @param coreName
	 * @param userId 
	 */
	public void copyConfFiles(String coreName, int runId, String userId) {
		String sourceDir = null;
		String destDir = this.environment.getProperty("app.file_dest")+runId;
		try {
			List<LookupModel> lookupList = lookupRepo.findBylookupTypeIdAndIsActive(1, 1); // lookup type id 1 for "CopySolrConfigFiles"
			Iterator<LookupModel>lookupDetails = lookupList.iterator();
			while (lookupDetails.hasNext()) {
				LookupModel lookupModel = (LookupModel) lookupDetails.next();
				sourceDir = this.environment.getProperty("app.solr_path") +coreName+"//conf//"+lookupModel.getValue();
				File src = new File(sourceDir);
				File dest = new File(destDir);
				FileUtils.copyFileToDirectory(src, dest);
			}
			
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
	}


	/**
	 * Get all search address from search_address table and find out last word of that address.
	 * @param runType 
	 * @param runName 
	 * @param userId 
	 * @return status
	 */
	public int getSAddress(int runType, String runName, String userId) {
		int status = 0;
		int runId = 0;
		String coreName;
		if (runType == 1) {
			coreName = this.environment.getProperty("app.core_name");
		}else {
			coreName = this.environment.getProperty("app.cersai_core_name");
		}
		
		RunMasterModel masterModel = new RunMasterModel();
		try {
			masterModel.setCoreName(coreName);
			masterModel.setCreatedBy(Integer.valueOf(userId));
			masterModel.setRemark(runName);
			masterModel.setRunTypeId(runType);
			runMasterRepo.save(masterModel);
			runId = masterModel.getRunId();
//		      copyConfFiles(coreName, runId);
			List<SearchAddressModel> searchList = searchAddressRepo.findAll();
			int counter = 0;
			status = 1;
			for (Iterator iterator = searchList.iterator(); iterator.hasNext();) {
				SearchAddressModel searchAddressModel = (SearchAddressModel) iterator.next();
				String searchAddress = searchAddressModel.getAddress().replace(":", "");
				String panNo = searchAddressModel.getPanNo();
				System.out.println(searchAddress);
				int saId = searchAddressModel.getSaId();
				if (!searchAddress.equalsIgnoreCase("")) {
					status = callNadmatAPI(runType, panNo, searchAddress, saId, runId, coreName, userId);
				}
				counter++;
//				if (counter == 500) {
//					break;
//				}
			}
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
	 *  Call NADMATApi for get solr response and insert response to run_result table on db.
	 * @param runType 
	 * @param searchAddress
	 * @param searchAddress 
	 * @param saId
	 * @param runId
	 * @param coreName 
	 * @param userId 
	 * @return status
	 */
	public int callNadmatAPI(int runType, String panNo, String searchAddress, int saId, int runId, String coreName, String userId) {
		int status = 1;
		List<RunResultModel> apiResponseList = new ArrayList<RunResultModel>();
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		String apiUrl = this.environment.getProperty("app.api_url");
		String rows = this.environment.getProperty("app.master_rows");
		try {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
		urlBuilder.addQueryParameter("searchAddress", searchAddress);
		urlBuilder.addQueryParameter("rows", rows);
		urlBuilder.addQueryParameter("coreName", coreName);
		urlBuilder.addQueryParameter("panNo", panNo);
		urlBuilder.addQueryParameter("runType", String.valueOf(runType));
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
				 if (runType == 1) {
					 if (masterList.length() == 1 && jsonList.get("amId").toString().equals("")) {
							UnprocessedAddressModel addressModel = new UnprocessedAddressModel();
							addressModel.setApiUrl(jsonList.get("apiUrl").toString());
							addressModel.setRunId(runId);
							addressModel.setSaId(saId);
							addressModel.setCreatedBy(Integer.valueOf(userId));
							unprocessedAddressRepo.save(addressModel);
						} else {
							RunResultModel resultModel = new RunResultModel();
							 resultModel.setRunId(runId);
							 resultModel.setSaId(saId);
							 resultModel.setMappingId(Integer.parseInt(jsonList.get("amId").toString()));
							 resultModel.setScore(jsonList.get("score").toString());
							 resultModel.setApiResponse(jsonList.get("apiResponse").toString());
							 if (runType != 1) {
								 resultModel.setHighlightResponse(jsonList.get("hlAddress").equals("") ? "" : jsonList.get("hlAddress").toString());
							}
							 resultModel.setApiUrl(jsonList.get("apiUrl").toString());
							 resultModel.setCreatedBy(Integer.valueOf(userId));
							 apiResponseList.add(resultModel);
						}
				} else {
					if (masterList.length() == 1 && jsonList.get("caId").toString().equals("")) {
						UnprocessedAddressModel addressModel = new UnprocessedAddressModel();
						addressModel.setApiUrl(jsonList.get("apiUrl").toString());
						addressModel.setRunId(runId);
						addressModel.setSaId(saId);
						addressModel.setCreatedBy(Integer.valueOf(userId));
						unprocessedAddressRepo.save(addressModel);
					} else {
						// set highlight text in cersai address
						String cersai = "";
						if (jsonList.get("hlAddress").toString() != "") {
							cersai = jsonList.get("cersaiAddress").toString();
							String hlAddress = jsonList.get("hlAddress").toString();
							Document document = Jsoup.parse(hlAddress);
							Elements paragraphs = document.getElementsByTag("em");
							Set<String> distWord = new HashSet<String>();
							for (Element hWord : paragraphs) {
								distWord.add(hWord.text());
							}
							for (String distHWord : distWord) {
								if (cersai.toLowerCase().contains(distHWord.toLowerCase())) {
									cersai = cersai.replace(distHWord,"<strong><em>" + distHWord + "</em></strong>");
								}
							}
						}
						System.err.println("cersai=== "+cersai);
						RunResultModel resultModel = new RunResultModel();
						 resultModel.setRunId(runId);
						 resultModel.setSaId(saId);
						 resultModel.setMappingId(Integer.parseInt(String.valueOf(jsonList.get("caId"))));
						 resultModel.setMappedAmId(jsonList.get("mappedAmId").equals("") ? 0 : Integer.parseInt(jsonList.get("mappedAmId").toString()));
						 resultModel.setScore(jsonList.get("score").toString());
						 resultModel.setApiResponse(jsonList.get("apiResponse").toString());
						 resultModel.setHighlightResponse(jsonList.get("hlAddress") == "" ? "" : jsonList.get("hlAddress").toString());
						 resultModel.setHighlightedCersai(cersai == "" ? jsonList.get("cersaiAddress").toString() : cersai);
						 resultModel.setApiUrl(jsonList.get("apiUrl").toString());
						 resultModel.setCreatedBy(Integer.valueOf(userId));
						 apiResponseList.add(resultModel);
					}
				}
				 
			 }
			 runResultRepo.saveAll(apiResponseList);
			 status = 2;
		} catch (Exception e) {
			status = 3;
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return status;
	}
	
	/**
	 * Get all distinct pan number from DB
	 * @return panList
	 */
	public List<String> getPanNumber() {
		List<String> panList =  searchAddressRepo.findAllPanNo();
		return panList;
		
	}
	
	/**
	 * Get Run Id from run_master table from DB
	 * @param runType 
	 * @return runMasterDetails
	 */
	public List<RunMasterModel> getRunId(int runType) {
		List<RunMasterModel> runMasterList =  runMasterRepo.findByRunTypeId(runType, Sort.by(Sort.Direction.DESC, "runId"));
		List<RunMasterModel> runMasterDetails = new ArrayList<RunMasterModel>();
		
		for (Iterator iterator = runMasterList.iterator(); iterator.hasNext();) {
			RunMasterModel runMasterModel = (RunMasterModel) iterator.next();
			// Populate only successfully created runId.
			if (runMasterModel.getStatus() == 2) {
				RunMasterModel runObj = new RunMasterModel();
				runObj.setRunId(runMasterModel.getRunId());
				runObj.setRemark(runMasterModel.getRemark());
				runMasterDetails.add(runObj);
			}
		}
		return runMasterDetails;
		
	}
	/**
	 * Get runType from lookup table from DB
	 * @return LookupDetails
	 */
	public List<LookupModel> getRunType() {
		List<LookupModel> lookupList = lookupRepo.findBylookupTypeIdAndIsActive(2, 1);
		List<LookupModel> LookupDetails = new ArrayList<LookupModel>();
		
		for (Iterator iterator = lookupList.iterator(); iterator.hasNext();) {
			LookupModel lookupSummay = (LookupModel) iterator.next();
			LookupModel lookupObj = new LookupModel();
			lookupObj.setCode(lookupSummay.getCode());
			lookupObj.setValue(lookupSummay.getValue());
			LookupDetails.add(lookupObj);
		}
		return LookupDetails;
	}

	
	/**
	 * Check the pan number is exist or not in user redis data if yes, get the data from redis based on pan number 
	 * else get searchAddress from DB after the insert into redis.
	 * @param panNo
	 * @param runId
	 * @param userId
	 * @param userName
	 * @param runType 
	 * @return responseMaster
	 */
	public ResultMasterDTO checkPanIsExistInRedis(String panNo, int runId, String userId, String userName, int runType) {
		LocalDateTime currentTime = LocalDateTime.now();
		ResultMasterDTO responseMaster = new ResultMasterDTO();
		List<SearchAddressDTO> searchAddress = new ArrayList<SearchAddressDTO>();
		boolean isPanExist = false;
		try {
			if (userRedisRepo.findByPanNo(panNo) != null) {
				UserRedisModel model =  userRedisRepo.findByPanNo(panNo);
				String username  =  model.getUserName();
				int redisUserId = model.getUserId();
				LocalDateTime createdTime = LocalDateTime.parse(model.getCreatedOn());
				// Release lock from redis if lock is 20mins older and insert new record into the redis.
				// Lock the Pan details with runType.
				if (runType == model.getRunType()) {
					if (ChronoUnit.SECONDS.between(createdTime, currentTime) > 1200) {
						//insert current pan into database and after clear redis data 
						insertUpdatedPan(panNo, userId, runType);
						clearRedisByPan(panNo, userId);
						searchAddress = getSearchAddress(panNo, runId, userId, userName, runType);
					}else {
						if (redisUserId == Integer.valueOf(userId)) {
							isPanExist = false;
						}else {
							isPanExist = true;
						}
						searchAddress = getPanSummaryFromRedis(panNo,userId);
						responseMaster.setUserName(username);
					}
				}else {
					searchAddress = getSearchAddress(panNo, runId, userId, userName, runType);
				}
			}else {
				searchAddress = getSearchAddress(panNo, runId, userId, userName, runType);
			}
			responseMaster.setIsPanExist(isPanExist);
			responseMaster.setSearchAddress(searchAddress);
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return responseMaster;
	}

	/**
	 * Get all search address based on selected pan number from DB and Insert in to redis.
	 * @param panNo
	 * @param runId
	 * @param userName 
	 * @param userId 
	 * @param runType 
	 * @return searchAddress
	 */
	public List<SearchAddressDTO> getSearchAddress(String panNo, int runId, String userId, String userName, int runType) {
		LocalDateTime timestamp = LocalDateTime.now();
		List<RedisModel> searchAddressDetails = new ArrayList<RedisModel>();
		List<SearchAddressDTO> searchAddress = new ArrayList<SearchAddressDTO>();
		RedisModel redisModel = new RedisModel();
		try {
			if (runType == 1) {
				List<Object[]> searchAddressList =  searchAddressRepo.findByMasterPanNoAndRunID(panNo, runId, runType);
				Iterator<Object[]> panDetailsList = searchAddressList.iterator();
				while (panDetailsList.hasNext()) {
					Object[] obj = panDetailsList.next();
					RedisModel objPan = new RedisModel();
					objPan.setSaId(Integer.valueOf(obj[0].toString()));
					objPan.setAddress(obj[1].toString());
					objPan.setMappingId(Integer.valueOf(obj[2].toString()));
					objPan.setDistrict(obj[3].toString());
					objPan.setTaluka(obj[4].toString());
					objPan.setState(obj[5].toString());
					objPan.setPincode(Integer.valueOf(String.valueOf(obj[6])));
					objPan.setScore(obj[7].toString());
					objPan.setApiResponse(obj[8].toString());
					objPan.setIsValidated(Integer.valueOf(obj[9].toString()));
					objPan.setArea(obj[10].toString());
					objPan.setRunType(runType);
					searchAddressDetails.add(objPan);
				}
			}else {
				List<Object[]> searchAddressList =  searchAddressRepo.findCersaiByPanNoAndRunID(panNo, runId, runType);
				Iterator<Object[]> panDetailsList = searchAddressList.iterator();
				while (panDetailsList.hasNext()) {
					Object[] obj = panDetailsList.next();
					RedisModel objPan = new RedisModel();
					objPan.setSaId(Integer.valueOf(obj[0].toString()));
					objPan.setAddress(obj[1].toString());
					objPan.setMappingId(Integer.valueOf(obj[2].toString()));
					objPan.setCersaiPanNo(obj[3].toString());
					objPan.setCersaiAddress(obj[4].toString());
					objPan.setArea(obj[5] == null ? "" : obj[5].toString());
					objPan.setDistrict(obj[6] == null ? "" : obj[6].toString());
					objPan.setState(obj[7] == null ? "" : obj[7].toString());
					objPan.setTaluka(obj[8] == null ? "" : obj[8].toString());
					objPan.setPincode(obj[9] == null ? 0 : Integer.valueOf(obj[9].toString()));
					objPan.setScore(obj[10].toString());
					objPan.setApiResponse(obj[11].toString());
					objPan.setHighlightedResponse(obj[12].toString());
					objPan.setIsValidated(Integer.valueOf(obj[13].toString()));
					objPan.setRunType(runType);
					searchAddressDetails.add(objPan);
				}
			}
				UserRedisModel userRedisModel = new UserRedisModel();
				userRedisModel.setPanNo(panNo);
				userRedisModel.setRunType(runType);
				userRedisModel.setUserId(Integer.valueOf(userId));
				userRedisModel.setUserName(userName);
				userRedisModel.setCreatedOn(String.valueOf(timestamp));
				userRedisRepo.insertRedis(userRedisModel);
				
				redisModel.setPanDetailsList(searchAddressDetails);
				redisModel.setPanNo(panNo);
				//call insert method in redisRepo class for insert into redis
				redisRepo.insertRedis(redisModel);
				searchAddress = getPanSummaryFromRedis(panNo,userId);
			
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return searchAddress;
		
	}
	
	/**
	 * get pan summary from redis based on pan num. and populate search Address
	 * @param panNo
	 * @param userId 
	 * @return searchAddress
	 */
	public List<SearchAddressDTO> getPanSummaryFromRedis(String panNo, String userId) {
		List<SearchAddressDTO> searchAddress = new ArrayList<SearchAddressDTO>();
		HashMap<Integer,String> searchAddressMap = new HashMap<Integer,String>();
		try {
			RedisModel panSummary = redisRepo.findByPanNo(panNo);
			for (RedisModel panSummaryList : panSummary.getPanDetailsList()) {
				if (searchAddressMap.containsKey(panSummaryList.getSaId())) {
					continue;
				} else {
					searchAddressMap.put(panSummaryList.getSaId(), panSummaryList.getAddress());
					SearchAddressDTO addressDTO = new SearchAddressDTO();
					addressDTO.setAddress(searchAddressMap.get(panSummaryList.getSaId()));
					addressDTO.setSaId(panSummaryList.getSaId());
					searchAddress.add(addressDTO);
				}
			}
			
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return searchAddress;
		
	}

	/**
	 * Populate master data on UI from redis based on selected search address.
	 * @param panNo
	 * @param saId
	 * @param userId 
	 * @return masterAddress
	 */
	public List<MasterDataDTO> getMasterAddress(String panNo, int saId, String userId) { 
		List<MasterDataDTO> masterAddress = new ArrayList<MasterDataDTO>();
		try {
			RedisModel panSummary = redisRepo.findByPanNo(panNo);
			List<RedisModel> panDetailsList = panSummary.getPanDetailsList();
			for (RedisModel panDetailsSummry : panDetailsList) {
				if (saId == panDetailsSummry.getSaId()) {
					MasterDataDTO objPan = new MasterDataDTO();
					objPan.setMappingId(panDetailsSummry.getMappingId());
					objPan.setCersaiPanNo(panDetailsSummry.getCersaiPanNo());
					objPan.setCersaiAddress(panDetailsSummry.getCersaiAddress());
//					objPan.sethAddress(panDetailsSummry.getHighlightedResponse());
					objPan.setArea(panDetailsSummry.getArea());
					objPan.setDistrict(panDetailsSummry.getDistrict());
					objPan.setTaluka(panDetailsSummry.getTaluka());
					objPan.setState(panDetailsSummry.getState());
					objPan.setPinCode(String.valueOf(panDetailsSummry.getPincode()));
					objPan.setScore(panDetailsSummry.getScore());
					objPan.setIsValidated(panDetailsSummry.getIsValidated());
					masterAddress.add(objPan);
				}
			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return masterAddress;
	}

	/**
	 * Take pan details based on pan number from redis and Validate master address based on serach addresses and update on redis with validated flag.
	 * @param panNo
	 * @param saId
	 * @param amId
	 * @param isValid
	 * @param userId 
	 * @return status
	 */
	public int validatedAddress(String panNo, int saId, int mappingId, int isValid, String userId) {
		int Status = 0;
		List<RedisModel> updatedPanSummary = new ArrayList<RedisModel>();
		try {
			RedisModel panSummary = redisRepo.findByPanNo(panNo);
			List<RedisModel> panDetailsList = panSummary.getPanDetailsList();
			for (RedisModel panDetailsSummry : panDetailsList) {
				if (saId == panDetailsSummry.getSaId() && mappingId == panDetailsSummry.getMappingId()) {
					RedisModel objPan = new RedisModel();
					objPan.setSaId(panDetailsSummry.getSaId());
					objPan.setAddress(panDetailsSummry.getAddress());
					objPan.setApiResponse(panDetailsSummry.getApiResponse());
					objPan.setMappingId(panDetailsSummry.getMappingId());
					objPan.setCersaiPanNo(panDetailsSummry.getCersaiPanNo());
					objPan.setCersaiAddress(panDetailsSummry.getCersaiAddress());
//					objPan.setHighlightedResponse(panDetailsSummry.getHighlightedResponse());
					objPan.setArea(panDetailsSummry.getArea());
					objPan.setDistrict(panDetailsSummry.getDistrict());
					objPan.setTaluka(panDetailsSummry.getTaluka());
					objPan.setState(panDetailsSummry.getState());
					objPan.setPincode(panDetailsSummry.getPincode());
					objPan.setScore(panDetailsSummry.getScore());
					objPan.setIsValidated(isValid);
					objPan.setRunType(panDetailsSummry.getRunType());
					updatedPanSummary.add(objPan);
				}else {
					RedisModel objPan = new RedisModel();
					objPan.setSaId(panDetailsSummry.getSaId());
					objPan.setAddress(panDetailsSummry.getAddress());
					objPan.setApiResponse(panDetailsSummry.getApiResponse());
					objPan.setMappingId(panDetailsSummry.getMappingId());
					objPan.setCersaiPanNo(panDetailsSummry.getCersaiPanNo());
					objPan.setCersaiAddress(panDetailsSummry.getCersaiAddress());
//					objPan.setHighlightedResponse(panDetailsSummry.getHighlightedResponse());
					objPan.setArea(panDetailsSummry.getArea());
					objPan.setDistrict(panDetailsSummry.getDistrict());
					objPan.setTaluka(panDetailsSummry.getTaluka());
					objPan.setState(panDetailsSummry.getState());
					objPan.setPincode(panDetailsSummry.getPincode());
					objPan.setScore(panDetailsSummry.getScore());
					objPan.setIsValidated(panDetailsSummry.getIsValidated());
					objPan.setRunType(panDetailsSummry.getRunType());
					updatedPanSummary.add(objPan);
				}
			}
			RedisModel redisModel = new RedisModel();
			redisModel.setPanNo(panNo);
			redisModel.setPanDetailsList(updatedPanSummary);
			redisRepo.insertRedis(redisModel);
			Status = 1;
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return Status;
	}

	/**
	 * When click on next/previous button from UI, update and save the pan details into database.
	 * @param panNo
	 * @param userId 
	 * @param runType 
	 */
	public void insertUpdatedPan(String panNo, String userId, int runType) {
		try {
			UserRedisModel model =  userRedisRepo.findByPanNo(panNo);
			if (model != null) {
				int redisUserId = model.getUserId();
				int redisRunType = model.getRunType();
				if (Integer.parseInt(userId) == redisUserId && runType == redisRunType) {
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					List<Integer> saIds = new ArrayList<Integer>();
					List<ValidatedSAModel> validateAddress = new ArrayList<ValidatedSAModel>();
					ValidatedSAModel updValidModel = new ValidatedSAModel();
					RedisModel panSummary = redisRepo.findByPanNo(panNo);
					if (panSummary != null) {
						List<RedisModel> panDetailsList = panSummary.getPanDetailsList();
						for (RedisModel panDetailsSummry : panDetailsList) {
							RedisModel redisSaId = new RedisModel();
							int saId = panDetailsSummry.getSaId();
							redisSaId.setSaId(saId);
							if (!saIds.contains(saId)) {
								saIds.add(saId);
							}

							if (panDetailsSummry.getIsValidated() == 1 && runType == panDetailsSummry.getRunType()) {
								ValidatedSAModel validModel = new ValidatedSAModel();
								validModel.setSaId(panDetailsSummry.getSaId());
								validModel.setMappingId(panDetailsSummry.getMappingId());
								validModel.setIsActive(1);
								validModel.setRunTypeId(runType);
								validModel.setCreatedBy(Integer.valueOf(userId));
								validateAddress.add(validModel);
							}
						}
						if (saIds.size() > 0) {
							updValidModel.setModifiedOn(String.valueOf(timestamp));
							updValidModel.setModifiedBy(Integer.valueOf(userId));
							String modifiedOn = updValidModel.getModifiedOn();
							int modifiedBy = updValidModel.getModifiedBy();
							validatedSARepo.updateSaId(saIds, modifiedOn, modifiedBy, runType);
						}
						validatedSARepo.saveAll(validateAddress);
					}
				}
			}
		
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
	}
	
	/**
	 * Clear pan details in redis based on selected pan number.
	 * @param panNo
	 * @param userId 
	 */
	public void clearRedisByPan(String panNo, String userId) {
		UserRedisModel model =  userRedisRepo.findByPanNo(panNo);
		if (model != null) {
			int redisUserId = model.getUserId();
			// check the same userId that time only clear redis.
			if (Integer.parseInt(userId) == redisUserId) {
				redisRepo.deleteByPan(panNo);
				userRedisRepo.deleteByPan(panNo);
			}
		}
	}
	
	/**
	 * Get cersai address,Id and state from cersai_address table. 
	 * @param runType
	 * @param runName
	 * @param userId
	 * @return status
	 */
	public int getCersaiAddress(int runType, String runName, String userId) {
		int status = 0;
		String coreName = this.environment.getProperty("app.core_name");
		
		try {
			List<CersaiAddressModel> searchList = cersaiAddressRepo.findAll();
			int counter = 0;
			status = 1;
			for (Iterator iterator = searchList.iterator(); iterator.hasNext();) {
				CersaiAddressModel searchAddressModel = (CersaiAddressModel) iterator.next();
				String cersaiAddress = searchAddressModel.getCersaiAddress().replace(":", "");
				String panNo = searchAddressModel.getPanNo();
				String state = searchAddressModel.getState();
				int caId = searchAddressModel.getCaId();
				if (!cersaiAddress.equalsIgnoreCase("")) {
					status = callNadmatAPIForCersai(panNo, cersaiAddress, caId, state, coreName, userId);
				}
				counter++;
//				if (counter == 5) {
//					break;
//				}
			}
		} catch (Exception e) {
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return status;
	}
	
	/**
	 * Call NadmatAPI for cersai with master and update 4 columns of Village, Taluka, Dist, State 
	 * in cersai_address_temp table from area master after matching.
	 * @param panNo
	 * @param cersaiAddress
	 * @param caId
	 * @param state
	 * @param coreName
	 * @param userId
	 * @return status
	 */
	public int callNadmatAPIForCersai(String panNo, String cersaiAddress, int caId, String state, String coreName, String userId) {
		int status = 1;
		List<RunResultModel> apiResponseList = new ArrayList<RunResultModel>();
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		String apiUrl = this.environment.getProperty("app.cersai_master_api_url");
		try {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
		urlBuilder.addQueryParameter("cersaiAddress", cersaiAddress);
		urlBuilder.addQueryParameter("coreName", coreName);
		urlBuilder.addQueryParameter("panNo", panNo);
		urlBuilder.addQueryParameter("state", state);
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
				 CersaiAddressModel updCersaiAddress = cersaiAddressRepo.findByCaId(caId);
				 updCersaiAddress.setMasterVillage(jsonList.get("area").toString());
				 updCersaiAddress.setMasterTaluka(jsonList.get("taluka").toString());
				 updCersaiAddress.setMasterDistrict(jsonList.get("district").toString());
				 updCersaiAddress.setMasterState(jsonList.get("state").toString());
				 updCersaiAddress.setMasterPincode(Integer.parseInt(jsonList.get("pinCode").toString()));
				 updCersaiAddress.setModifiedBy(Integer.parseInt(userId));
				 cersaiAddressRepo.save(updCersaiAddress);
			 }
			 status = 2;
		} catch (Exception e) {
			status = 3;
			Config.insertErrorLog(Thread.currentThread().getStackTrace()[1].getMethodName(), e, userId);
		}
		return status;
	}
}



