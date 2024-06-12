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
<link
	href="${pageContext.servletContext.contextPath }/assets/css/admin/guestbook.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/admin/include/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<div id="site-form">

					<h2>방명록 관리 페이지</h2>
					<ul>
						<c:forEach var="vo" items="${list}" varStatus="status">
							<table>
								<tr>
									<td>[${fn:length(list) - status.index}]</td>
									<td>${fn:replace(fn:replace(vo.name, ">", "&gt;"), "<", "&lt;")}</td>
									<td>${fn:replace(fn:replace(vo.reg_time, ">", "&gt;"), "<", "&lt;")}</td>
									<td><a
										href="${pageContext.request.contextPath}/admin/guestbook/delete/${vo.no}">삭제</a></td>
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
			<c:import url="/WEB-INF/views/admin/include/navigation.jsp">
				<c:param name="menu" value="guestbook" />
			</c:import>
		</div>
	</div>
</body>
</html>