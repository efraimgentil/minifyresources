<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Minify Resources</title>

<jsp:include page="/WEB-INF/template/_head.jsp"></jsp:include>
</head>
<body>

	<div style="margin: 0 30px;">
		<h1 style="margin: 0px">Sources</h1>
		<div style="border: 1px solid black;">
			<span style="white-space: pre"><c:out value="${sources}"></c:out></span>
		</div>
	</div>

</body>
</html>