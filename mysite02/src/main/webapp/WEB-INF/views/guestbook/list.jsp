<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.saltlux.mysite.vo.Guestbook02Vo"%>
<%@page import="com.saltlux.mysite.dao.Guestbook02Dao"%>
<%@page import="java.util.List"%>
<%
List<Guestbook02Vo> list = new Guestbook02Dao().findAll();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>방명록</title>
<link href="${pageContext.request.contextPath }/assets/css/guestbook.css"
	rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath }/assets/css/mysite.css"
	rel="stylesheet" type="text/css">
</head>
<body>

	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<div id="site-introduction">					
					
					<c:forEach items="${list}" var='vo' varStatus='status'>
					<br>
					<table width=510 border=1>
						<tr>
							<td>[${vo.no}]
							</td>
							<td>${vo.name }</td>
							<td>${vo.reg_date}</td>
							<td>
							<a href="${pageContext.request.contextPath }/guestbook?a=deleteform&no=${vo.no}">삭제</a>
							</td>
						</tr>
						<tr>
							<td colspan=4><pre>${vo.contents }</pre></td>
						</tr>
					</table>
					</c:forEach>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>


</body>
</html>