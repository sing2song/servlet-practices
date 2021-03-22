<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.request.contextPath }/board?a=search" method="post">
					<input type="text" id="kwd" name="search" value="">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					
					<c:set var="count" value="${fn:length(list)}"/>
					<c:forEach items="${list}" var='vo' varStatus='status'>
					
					<tr>
						<td>${vo.no}</td>
						<td>
						<c:choose>
							<c:when test="${vo.flag=='1'}">
								<c:if test="${vo.depth==0}">
									<span style="color:gray;">[삭제된 글입니다]</span>
								</c:if>		
								<c:if test="${vo.depth>0}">
									<img src="${pageContext.request.contextPath }/assets/images/reply.png" style="text-align:left;"/>
									<span style="color:gray;">[삭제된 답글입니다]</span>
								</c:if>								
							</c:when>
												
							<c:otherwise>
								<a href="${pageContext.request.contextPath}/board?a=look&no=${vo.no}" style="text-align:left; padding-left:${vo.depth*20}px;">
									<c:if test="${vo.depth>0}">
									<img src="${pageContext.request.contextPath }/assets/images/reply.png" style="text-align:left;"/>
									</c:if>
									${vo.title}											
								</a>
							</c:otherwise>
						</c:choose>		
						</td>
						<td>${vo.writer}</td>
						<td>${vo.hit}</td>
						<td>${vo.regDate}</td>
						<td>
						<c:if test="${vo.email==authUser.email}">
						<a href="${pageContext.request.contextPath}/board?a=deleteform&no=${vo.no}" class="del">삭제</a>
						</c:if>
						</td>
					</tr>
					
					</c:forEach>
				
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						${boardPaging.pagingHTML}						
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<c:if test="${authResult!='fail'}">
					<div class="bottom">
						<a href="${pageContext.request.contextPath }/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>