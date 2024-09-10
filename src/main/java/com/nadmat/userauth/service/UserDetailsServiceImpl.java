package com.nadmat.userauth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.nadmat.userauth.model.User;
import com.nadmat.userauth.repo.UserRepository;

/**
 *  This class is used in order to lookup the user name, password and GrantedAuthorities for any given user.
 * 
 * @author Vishal 
 * @since 2022-03-26
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    
    /**
     * It's used to retrieve user-related data from database and load details about the user during authentication
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsActive(username, 1);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}