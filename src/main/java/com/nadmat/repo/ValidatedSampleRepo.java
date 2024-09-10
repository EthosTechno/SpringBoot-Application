package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.model.ValidatedSampleModel;

public interface ValidatedSampleRepo  extends JpaRepository<ValidatedSampleModel, Integer>{

	List<ValidatedSampleModel> findBySmId(int smId);
	
	List<ValidatedSampleModel> deleteBySmId(int smId);
}
