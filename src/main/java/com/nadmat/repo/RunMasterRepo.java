package com.nadmat.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.model.RunMasterModel;

public interface RunMasterRepo extends JpaRepository<RunMasterModel, Integer>{

	RunMasterModel findByRunId(int runId);
	
	List<RunMasterModel> findByRunTypeId(int runTypeId, Sort sort);
}
