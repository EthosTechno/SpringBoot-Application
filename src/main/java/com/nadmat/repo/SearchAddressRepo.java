package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nadmat.model.SearchAddressModel;

public interface SearchAddressRepo extends JpaRepository<SearchAddressModel, Integer> {

	@Query(value = "select distinct pan_no from search_address sa", nativeQuery = true)
	List<String> findAllPanNo();
	
//	@Query(value = "select distinct pan_no from search_address sa \r\n"
//			+ "left join run_result rr on rr.sa_id = sa.sa_id and rr.run_id = (SELECT MAX(rr2.run_id) FROM run_result rr2)order by rr.score desc;", nativeQuery = true)
//	List<String> findAllPanNo();
	
	@Query(value = "select sa.sa_id as sa_id, sa.address as address, am.am_id as am_id, am.district as district, am.taluka as taluka, \r\n"
			+ "am.state as state, am.pincode as pincode,rr.score as score, rr.api_response as api_response,\r\n"
			+ "(case when vsa.sa_id is not null and vsa.mapping_id is not null then 1 else 0 end ) as isValidated, am.area as area from v_run_result rr \r\n"
			+ "inner join search_address sa on sa.sa_id = rr.sa_id \r\n"
			+ "inner join area_master am on am.am_id = rr.mapping_id \r\n"
			+ "left join validated_search_address vsa on vsa.mapping_id = am.am_id and vsa.sa_id = sa.sa_id and vsa.run_type_id = rr.run_type_id and vsa.is_active = 1\r\n"
			+ "where sa.pan_no  = :pan_no and run_id = :run_id and rr.run_type_id = :run_type\r\n"
			+ "union \r\n"
			+ "select sa.sa_id as sa_id, sa.address as address, am.am_id as am_id, am.district as district, am.taluka as taluka, \r\n"
			+ "am.state as state, am.pincode as pincode,rr.score as score, rr.api_response as api_response,\r\n"
			+ "(case when vsa.sa_id is not null and vsa.mapping_id is not null then 1 else 0 end ) as isValidated, am.area as area  from v_run_result rr \r\n"
			+ "inner join search_address sa on sa.sa_id = rr.sa_id \r\n"
			+ "inner join area_master am on am.am_id  = rr.mapping_id \r\n"
			+ "right join validated_search_address vsa on vsa.mapping_id = am.am_id and vsa.sa_id = sa.sa_id and vsa.run_type_id = rr.run_type_id  and vsa.is_active = 1\r\n"
			+ "where sa.pan_no  = :pan_no and run_id != :run_id and rr.run_type_id = :run_type", nativeQuery = true)
	
	List<Object[]> findByMasterPanNoAndRunID(@Param("pan_no") String panNo, @Param("run_id") int runId, @Param("run_type") int runType);
	
	@Query(value = "select sa.sa_id as sa_id, sa.address as address, ca.ca_id as ca_id, ca.pan_no as pan_no, rr.highlighted_cersai as cersai_address, am.area as area, am.district as district, \r\n"
			+ "am.state as state,am.taluka as taluka, am.pincode as pincode,rr.score as score, rr.api_response as api_response, rr.highlight_response,\r\n"
			+ "(case when vsa.sa_id is not null and vsa.mapping_id is not null then 1 else 0 end ) as isValidated from v_run_result rr \r\n"
			+ "inner join search_address sa on sa.sa_id = rr.sa_id \r\n"
			+ "inner join cersai_address ca on ca.ca_id = rr.mapping_id \r\n"
			+ "left join area_master am on am.am_id = rr.mapped_am_id \r\n"
			+ "left join validated_search_address vsa on vsa.mapping_id = ca.ca_id and vsa.sa_id = sa.sa_id and vsa.is_active = 1 and vsa.run_type_id = rr.run_type_id\r\n"
			+ "where sa.pan_no  = :pan_no and run_id = :run_id and rr.run_type_id = :run_type\r\n"
			+ "union \r\n"
			+ "select sa.sa_id as sa_id, sa.address as address, ca.ca_id as ca_id, ca.pan_no as pan_no, rr.highlighted_cersai as cersai_address, am.area as area, am.district as district, \r\n"
			+ "am.state as state, am.taluka as taluka, am.pincode as pincode,rr.score as score, rr.api_response as api_response, rr.highlight_response,\r\n"
			+ "(case when vsa.sa_id is not null and vsa.mapping_id is not null then 1 else 0 end ) as isValidated from v_run_result rr \r\n"
			+ "inner join search_address sa on sa.sa_id = rr.sa_id \r\n"
			+ "inner join cersai_address ca on ca.ca_id = rr.mapping_id \r\n"
			+ "left join area_master am on am.am_id = rr.mapped_am_id \r\n"
			+ "right join validated_search_address vsa on vsa.mapping_id = ca.ca_id and vsa.sa_id = sa.sa_id and vsa.is_active = 1 and vsa.run_type_id = rr.run_type_id\r\n"
			+ "where sa.pan_no  = :pan_no and run_id != :run_id and rr.run_type_id = :run_type", nativeQuery = true)
	
	List<Object[]> findCersaiByPanNoAndRunID(@Param("pan_no") String panNo ,@Param("run_id") int runId, @Param("run_type") int runType);
	
}
