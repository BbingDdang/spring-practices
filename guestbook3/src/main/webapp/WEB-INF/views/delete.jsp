<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content = "text/html" charset="UTF-8">
<title>방명록</title>
</head>
<body>
	<form method="post" action="${pageContext.servletContext.contextPath }/delete/${no}">
	<table>
		<tr>
			<td>비밀번호</td>
			<td><input type = "password" name="password"></td>
			<td><input type = "submit" value="제출"></td>
		</tr>
	</table>
	</form>
	<br><br>
	<a href = "${pageContext.servletContext.contextPath }">메인으로 돌아가기</a>
</body>
</html>