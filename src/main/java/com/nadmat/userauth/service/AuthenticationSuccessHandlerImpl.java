package com.nadmat.userauth.service;

import java.io.IOException;
import java.net.InetAddress;
import java.security.Principal;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nadmat.common.model.LoginHistory;
import com.nadmat.common.repo.LoginHistoryRepo;
import com.nadmat.userauth.repo.UserRepository;

/**
 * This class is used to custom AuthenticationSuccessHandler which will do the redirection based on the roles and store the user details in the session storage.
 * 
 * @author Vishal 
 * @since 2022-03-26
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 */

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

	 @Autowired 
	 HttpSession session;
	 
	 @Autowired
	 UserRepository userRepository;
	 
	 @Autowired
	 LoginHistoryRepo loginHistoryRepo;
	 
	
	 /**
	  * After successful authentication, set the user details in the session and redirect to the dashboard page 
	  */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		/* Principal is given the currently logged in user object */
		String userName = "";
        if(authentication.getPrincipal() instanceof Principal) {
             userName = ((Principal) authentication.getPrincipal()).getName();
        }else {
            userName = ((User) authentication.getPrincipal()).getUsername();
        }
        com.nadmat.userauth.model.User user = userRepository.findByUsernameAndIsActive(userName, 1);
        int userId = user.getId();
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress =  inetAddress.getHostAddress();

        /* store the logged in user details in session storage  */
		session.setAttribute("user_id", userId);
        session.setAttribute("user_name", userName);
        session.setAttribute("is_login", "1");
        session.setAttribute("ip_address", ipAddress);
        
        /* It is used to insert login history log into database While successfully login and set loginHistId into session. */
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUserName(userName);
		loginHistory.setIpAddress(ipAddress);
		loginHistory.setLoginDate(timestamp);
		loginHistoryRepo.save(loginHistory);
		int loginHistId = loginHistory.getLoginHistoryId();
		session.setAttribute("login_history_id", loginHistId);
                       
       response.sendRedirect(request.getContextPath() +"/dashboard");
	}

}
