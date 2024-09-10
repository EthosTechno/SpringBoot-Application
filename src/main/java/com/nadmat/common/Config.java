package com.nadmat.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nadmat.common.model.ErrorLogDTO;
import com.nadmat.common.model.LoginHistory;
import com.nadmat.common.repo.InsertErrorLogRepo;
import com.nadmat.common.repo.LoginHistoryRepo;

/**
 * @author Vishal
 * @since 28-03-2022
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 25-05-22		Vishal		- Create insertErrorLog and insertLoginHistory method for Insert error log and login history into database.
 */

@Component
public class Config {

	private static InsertErrorLogRepo insertErrorLog;
	private static LoginHistoryRepo loginHistoryLog;
	private static Environment environment;

	@Autowired
	InsertErrorLogRepo insertErrorLogRepo;
	
	@Autowired
	LoginHistoryRepo loginHistoryRepo;
	
	@Autowired
	Environment env;
	
	/*
	 * We can't use to @Autowired in static method so here we used to @PostConstruct
	 * method below is always called before anything can access in the Static
	 * method.
	 */
	
	@PostConstruct
	public void init() {
		Config.insertErrorLog = insertErrorLogRepo;
		Config.loginHistoryLog = loginHistoryRepo;
		Config.environment = env;
	}

	public static String URL = "";
	//public static final String APP_VERSION = "2.0.0.0";
	
	/**
	 * return response in json format
	 * @param status
	 * @param message
	 * @return
	 */
	public static String doResponse(int status, String message) {
		return "{\"status\":\""+status+"\", \"message\": \""+message+"\"}";
	}

	/* Date Time Objects */
	public static final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// get system date and time using Date API against session
	public static String getSystemDateTime(HttpSession session) {
		return (dateTime.format(new Date()));
	}
	
	/* get app version from application properties*/		
	public static String appVersion() {
		return environment.getProperty("app.version");
	}
	

	/**
	 * This method is used to insert error log into database While having any
	 * error occur during the project its captured in database
	 * @param source
	 * @param e
	 * @param userId
	 */

	public static void insertErrorLog(String source, Exception e, String userId) {
		ErrorLogDTO errorLogDTO = new ErrorLogDTO();
		try {
			e.printStackTrace();
			String exceptionName = e.getClass().getName();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stackTrace = sw.toString();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			errorLogDTO.setErrException(exceptionName);
			errorLogDTO.setErrSource(source);
			errorLogDTO.setStackTrace(stackTrace);
			errorLogDTO.setUserId(userId);
			errorLogDTO.setCreatedOn(String.valueOf(timestamp));
			errorLogDTO.setCreatedBy(userId);
			insertErrorLog.save(errorLogDTO);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	/**
	 * This method is used to update logout date/time in 'login_history' table into
	 * database While successfully logout.
	 * 
	 * @param loginHistId
	 */
	public static void insertLoginHistory(int loginHistId) {
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			LoginHistory loginHistoryDetails = loginHistoryLog.findByLoginHistoryId(loginHistId);
			loginHistoryDetails.setLogoutDate(timestamp);
			loginHistoryLog.save(loginHistoryDetails);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * This method is used to check session for logged in user.
	 * @param session
	 * @return
	 */
	public static boolean session_check(HttpSession session) {
		if (session.getAttribute("is_login") == null || !session.getAttribute("is_login").equals("1")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This method is used to return status code and message for log
	 * @param code
	 * @param e
	 * @return
	 */
	public static String log(int code, Exception e) {
		if(code == 403) {
			return doResponse(403, "Authentication Failure");
		} 
		return doResponse(1000, "General Failure.");
	}

}
