package com.nadmat.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nadmat.model.SampleMasterModel;

public interface SampleMasterRepo extends JpaRepository<SampleMasterModel, Integer>{

	List<SampleMasterModel> findFirstByProcessStatus(int processStatus, Sort sort);
	
	List<SampleMasterModel> findBySampleType(int sampleType);
	
	SampleMasterModel findBySmId(int smId);
	
	@Query(value = "select sm_id, search_string, md_id from sample_master sm where sample_type = :sampleType", nativeQuery = true)
	List<Object[]> getsampleMasterBySampleType(@Param("sampleType") int sampleType);
	
	@Query(value = "SELECT sm.sm_id, sm.search_string, sm.md_id FROM sample_master sm \r\n"
			+ "left join sample_result sr on sm.sm_id =sr.sm_id and sr.run_id = :runId \r\n"
			+ "where sm.sample_type = :sampleType and sm.is_active =1 and sr.sm_id is NULL", nativeQuery = true)
	List<Object[]> getUnprocessSampleMaster(@Param("runId") int runId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, sm.process_status as process_status "
			+ "from sample_master sm where sm.sm_id = :ui_smId and sample_type = :sampleType", nativeQuery = true)
	List<Object[]> getFirstSampleMaster(@Param("ui_smId") int uiSmId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, sm.process_status as process_status "
			+ "from sample_master sm where sm.sm_id > :ui_smId and sample_type = :sampleType order by sm_id asc limit 1", nativeQuery = true)
	List<Object[]> getFirstSampleMasterASC(@Param("ui_smId") int uiSmId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, sm.process_status as process_status "
			+ "from sample_master sm where sm.sm_id < :ui_smId and sample_type = :sampleType order by sm_id desc limit 1", nativeQuery = true)
	List<Object[]> getFirstSampleMasterDESC(@Param("ui_smId") int uiSmId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, sm.process_status as process_status from sample_master sm \r\n"
			+ "where process_status in (0,1) and sample_type = :sampleType and (TIMESTAMPDIFF(MINUTE,sm.modified_on ,NOW()) > 10 OR sm.modified_on is null or sm.modified_by = :userId) \r\n"
			+ "order by (case when sm.modified_by = :userId then 0 else 1 end), sm_id asc limit 1", nativeQuery = true)
	List<Object[]> getFirstByProcessStatusASC(@Param("userId") int userId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, sm.process_status as process_status from sample_master sm \r\n"
			+ "where process_status in (0,1) and sample_type = :sampleType and (TIMESTAMPDIFF(MINUTE,sm.modified_on ,NOW()) > 10 OR sm.modified_on is null or sm.modified_by = :userId) \r\n"
			+ "order by (case when sm.modified_by = :userId then 0 else 1 end), sm_id desc limit 1", nativeQuery = true)
	List<Object[]> getFirstByProcessStatusDESC(@Param("userId") int userId, @Param("sampleType") int sampleType);
	
	@Query(value = "UPDATE sample_master SET  process_status=:processStatus, modified_by=:modified_by, modified_on=current_timestamp() WHERE sm_id=:smId", nativeQuery = true)
	void updateStatus(@Param("modified_by") int modifiedBy, @Param("processStatus") int processStatus, @Param("smId") int smId);
	
	// get row number for progess bar when display toggle is on.
	@Query(value = "select row_num from v_sample_master vsm where vsm.sm_id = :smId and sample_type = :sampleType", nativeQuery = true)
	int getProgressBarByDispValidated(@Param("smId") int smId, @Param("sampleType") int sampleType);
	
	// get validated addresses for progress bar when display toggle is off.
	@Query(value = "select count(*) from v_sample_master vsm where vsm.process_status = 2 and sample_type = :sampleType", nativeQuery = true)
	int getProgressBarByNotDispValidated(@Param("sampleType") int sampleType);
	
	// get total number of addresses 
	@Query(value = "select count(*) from v_sample_master vsm where sample_type = :sampleType", nativeQuery = true)
	int getTotalSearchStr(@Param("sampleType") int sampleType);
	
	@Query(value = "select sm_id, process_status from sample_master sm where search_string = :searchString and sample_type = :sampleType", nativeQuery = true)
	List<Object[]> getsmIdBySearchStr(@Param("searchString") String searchString, @Param("sampleType") int sampleType);
	

}
