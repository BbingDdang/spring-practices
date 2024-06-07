<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn"%>
<%@ page import="com.poscodx.mysite.vo.GuestbookVo"%>
<%@ page import="java.util.List"%>
<%-- <%@page import="com.poscodx.mysite.dao.GuestbookDao"%> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	pageContext.setAttribute("newline", "\n");
%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<form action="${pageContext.request.contextPath }/guestbook/add" method="post">
					<input type="hidden" name="a" value="insert">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="contents"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<ul>
					<c:forEach var="vo" items="${list}" varStatus="status">
						<table>
							<tr>
								<td>[${fn:length(list) - status.index}]</td>
								<td>${fn:replace(fn:replace(vo.name, ">", "&gt;"), "<", "&lt;")}</td>
								<td>${fn:replace(fn:replace(vo.reg_time, ">", "&gt;"), "<", "&lt;")}</td>
								<td><a href="${pageContext.request.contextPath}/guestbook/delete/${vo.no}">삭제</a></td>
							</tr>
							<tr>
								<td colspan=4>
									${fn:replace(fn:replace(fn:replace(vo.contents, ">", "&gt;"), "<", "&lt;"), newline, "<br>")}
								</td>
							</tr>
						</table>
					</c:forEach>
				</ul>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>