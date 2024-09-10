<%@page import="java.sql.Connection"%>
<%@page import="com.nadmat.common.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%
	if(session!=null){
		try {
			Config.insertLoginHistory(Integer.valueOf(String.valueOf(session.getAttribute("login_history_id"))));
			session.invalidate();
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	response.sendRedirect(request.getContextPath());
%>