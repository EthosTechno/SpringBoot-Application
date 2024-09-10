package com.nadmat.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nadmat.model.CersaiAddressModel;

public interface CersaiAddressRepo extends JpaRepository<CersaiAddressModel, Integer>{

	CersaiAddressModel findByCaId(int caId);
	
	@Query(value = "select * from cersai_address_temp cat where mod(ca_id , 7)=0", nativeQuery = true)
	List<CersaiAddressModel> findRandomAddress();

}

