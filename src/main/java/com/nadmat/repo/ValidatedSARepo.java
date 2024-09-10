package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nadmat.model.ValidatedSAModel;

public interface ValidatedSARepo  extends JpaRepository<ValidatedSAModel, Integer>{
	
	@Query(value = "UPDATE validated_search_address SET is_active=0, modified_by= :modified_by, modified_on = :modified_on WHERE sa_id in (:sa_id) and run_type_id = :runType", nativeQuery = true)
	List<String> updateSaId(@Param("sa_id") List<Integer> saIds, @Param("modified_on") String modifiedOn, @Param("modified_by") int modifiedBy,  @Param("runType") int runType);
}
