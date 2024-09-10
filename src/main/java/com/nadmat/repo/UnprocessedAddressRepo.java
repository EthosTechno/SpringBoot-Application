package com.nadmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.model.UnprocessedAddressModel;

public interface UnprocessedAddressRepo extends JpaRepository<UnprocessedAddressModel, Integer>{

}
