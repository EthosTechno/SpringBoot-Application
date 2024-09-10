package com.nadmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.model.RunResultModel;

public interface RunResultRepo extends JpaRepository<RunResultModel, Integer> {

}
