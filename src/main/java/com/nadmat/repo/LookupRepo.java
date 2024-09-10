package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nadmat.model.LookupModel;


public interface LookupRepo extends JpaRepository<LookupModel, Integer>{

	List<LookupModel> findBylookupTypeIdAndIsActive(int lookupTypeId, int isActive);
	
	@Query(value = "select code from lookup l where lookup_type_id = 4 and is_active =1", nativeQuery = true)
	int getDefaultRunId();
}
