<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-06
  Time: 오후 2:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니 비우기</title>
</head>
<body>
<h1>장바구니 비우기</h1>
<p>장바구니를 비우시겠습니까?</p>
<form action="${pageContext.request.contextPath}/shopping/Cancel" method="post">
    <input type="submit" value="예">
</form>
    <a href="${pageContext.request.contextPath}/shopping/Look">장바구니 보기</a>
</body>
</html>

