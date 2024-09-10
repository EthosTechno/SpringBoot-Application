package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nadmat.model.SampleResultModel;

public interface SampleResultRepo extends JpaRepository<SampleResultModel, Integer>{

	List<SampleResultModel> findBySmId(int smId);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, md.md_id as md_id, md.search_string as master_string, \r\n"
			+ "sr.highlight_response as highlighted_address, sr.score as score,\r\n"
			+ "(case when vs.sm_id is not null and vs.md_id is not null then 1 else 0 end ) as isValidated from sample_result sr\r\n"
			+ "inner join sample_master sm on sm.sm_id = sr.sm_id \r\n"
			+ "inner join master_data md on md.md_id = sr.md_id \r\n"
			+ "left join validated_sample vs on vs.md_id = md.md_id and vs.sm_id = sm.sm_id and vs.is_active = 1\r\n"
			+ "where sm.sm_id = :smId and sr.run_id = (SELECT max(run_id) from sample_result sr inner join sample_master sm on sm.sm_id = sr.sm_id and sm.sample_type = :sampleType)\r\n"
			+ "order by isValidated desc, sr.score desc", nativeQuery = true)
	
	List<Object[]> getSmapleMasterBySmId(@Param("smId") int smId, @Param("sampleType") int sampleType);
	
	@Query(value = "select sm.sm_id as sm_id, sm.search_string as search_string, md.md_id as md_id, md.search_string as master_string, \r\n"
			+ "sr.highlight_response as highlighted_address, sr.score as score,\r\n"
			+ "0 as isValidated from sample_result sr\r\n"
			+ "inner join sample_master sm on sm.sm_id = sr.sm_id \r\n"
			+ "inner join master_data md on md.md_id = sr.md_id \r\n"
			+ "where sm.sm_id = :smId and sr.run_id = "
			+ "(SELECT max(run_id) from sample_result sr inner join sample_master sm on sm.sm_id = sr.sm_id and sm.sample_type = :sampleType)", nativeQuery = true)
	
	List<Object[]> getSmapleMasterBySmIdAndPS(@Param("smId") int smId, @Param("sampleType") int sampleType);
}
