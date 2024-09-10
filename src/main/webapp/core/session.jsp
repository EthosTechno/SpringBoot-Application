<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	try {
		if(!session.getAttribute("is_login").equals("1")) {
			response.sendRedirect(request.getContextPath());
			return;
		}
	} catch (Exception e) {
		response.sendRedirect(request.getContextPath());
		return;
	}
%>