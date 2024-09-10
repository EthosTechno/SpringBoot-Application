package com.nadmat.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.common.model.ErrorLogDTO;

/**
 * @author Vishal
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     Author    Comment
 * ---------------------------------------------------------------------------
 * 
 */
public interface InsertErrorLogRepo extends JpaRepository<ErrorLogDTO, Integer>{
    		 
}
