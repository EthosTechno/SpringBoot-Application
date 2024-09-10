package com.nadmat.userauth.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nadmat.userauth.model.User;

/**
 * @author Vishal
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     Author    Comment
 * ---------------------------------------------------------------------------
 * 
 */

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findById(int id);
	User findByIdAndIsActive(int id, int isActive);
	User findByUsernameAndIsActive(String username, int isActive);
	
}


