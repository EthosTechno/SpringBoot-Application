package com.nadmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.model.UnprocessedSampleModel;

public interface UnprocessedSampleRepo extends JpaRepository<UnprocessedSampleModel, Integer>{

}
