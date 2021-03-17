<%@page import="com.soltlux.guestbook01.dao.Guestbook01Dao"%>
<%@page import="com.soltlux.guestbook01.vo.Guestbook01Vo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
request.setCharacterEncoding("utf-8");

	String name = request.getParameter("name");
	String password = request.getParameter("password");
	String contents = request.getParameter("contents");
	
	Guestbook01Vo vo = new Guestbook01Vo();
	
	vo.setName(name);
	vo.setPassword(password);
	vo.setContents(contents);
	
	new Guestbook01Dao().insert(vo);
	
	response.sendRedirect("/guestbook01");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>성공적으로 등록되었습니다.</h1>
</body>
</html>