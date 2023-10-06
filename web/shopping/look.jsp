<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-06
  Time: 오전 11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="shopping.MenuItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니 현황</title>
</head>

<body>

<!-- 여기 수정: basketList를 스크립트릿 안에 넣어줍니다 -->
<%
    List<MenuItem> basketList = (List<MenuItem>) request.getAttribute("basketList");
%>

<% if (basketList != null && !basketList.isEmpty()) { %>

<table border="1">
    <tr>
        <th>제품명</th>
        <th>수량</th>
    </tr>

    <% for (MenuItem menuItem : basketList) { %>
    <tr>
        <td><%= menuItem.getProductName() %></td>
        <td><%= menuItem.getQuantity() %></td>
    </tr>
    <% } %>

</table>

<% } else { %>

<p>장바구니 데이터를 가져올 수 없습니다.</p>

<!-- 에러 메시지가 있다면 출력 -->
<% if(request.getAttribute("errorMessage") != null) { %>
<p>Error details: <%= request.getAttribute("errorMessage") %> </p>
<% } %>

<% } %>

</body>

</html>


