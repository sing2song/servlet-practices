<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.saltlux.guestbook.vo.Guestbook02Vo"%>
<%@page import="java.util.List"%>
<%
List<Guestbook02Vo> list = (List<Guestbook02Vo>)request.getAttribute("list");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>방명록</title>
</head>
<body>
	<form action="<%=request.getContextPath()%>/GuestbookServlet?a=add" method="post">
		<table border=1 width=500>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" value=""></td>
				<td>비밀번호</td>
				<td><input type="password" name="password" value=""></td>
			</tr>
			<tr>
				<td colspan=4><textarea name="contents" cols=60 rows=5></textarea></td>
			</tr>
			<tr>
				<td colspan=4 align=right><input type="submit" VALUE="확인"></td>
			</tr>
		</table>
	</form>
	<%
	int count = list.size();
		int index = 0;
		for (Guestbook02Vo vo : list) {
	%>
	<br>
	<table width=510 border=1>
		<tr>
			<td>[<%=count-index++ %>]</td>
			<td><%=vo.getName()%></td>
			<td><%=vo.getReg_date()%></td>
			<td><a href="<%=request.getContextPath() %>/GuestbookServlet?a=deleteform&no=<%=vo.getNo()%>">삭제</a></td>
		</tr>
		<tr>
			<td colspan=4>
			<pre><%=vo.getContents()%></pre>
			</td>
		</tr>
	</table>
	<%
		}
	%>
</body>
</html>