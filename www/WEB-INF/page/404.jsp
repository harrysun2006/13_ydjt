<%@ page contentType="text/html;charset=UTF-8"%>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>404 - 页面不存在</title>
</head>
<body>
<div>
	<div><h1>页面不存在.</h1></div>
	<div><a href="${pageContext.request.contextPath}/">返回首页</a></div>
</div>
</body>
</html>