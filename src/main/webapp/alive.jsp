<%@page import="com.nadmat.common.Config"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	try {
		if(session.getAttribute("is_login").equals("1")) {
			session.setAttribute("is_login", "1");
			out.print(Config.doResponse(200, "ok"));
		} else {
			out.print(Config.doResponse(403, "Authentication Failure"));
		}
	} catch(Exception e) {
		out.print(Config.log(403, e));
	}
%>