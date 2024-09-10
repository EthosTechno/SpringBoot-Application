package com.nadmat.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.common.model.LoginHistory;


/** 
 * @author Vishal
 * 
 * Change log
 * ---------------------------------------------------------------------------
 * Date     Author    Comment
 * ---------------------------------------------------------------------------
 * 
 */
public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Integer>{
	LoginHistory findByLoginHistoryId(int loginHistoryId);
}
