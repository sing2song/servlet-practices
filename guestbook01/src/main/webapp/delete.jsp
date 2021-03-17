<%@page import="com.soltlux.guestbook01.dao.Guestbook01Dao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
request.setCharacterEncoding("utf-8");

	String no = request.getParameter("no");
	String password = request.getParameter("password");
	if(new Guestbook01Dao().delete(no,password))	
		response.sendRedirect("/guestbook01");
	else
		response.sendRedirect("/guestbook01/deleteform.jsp?no="+no);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>성공적으로 삭제되었습니다.</h1>
</body>
</html>