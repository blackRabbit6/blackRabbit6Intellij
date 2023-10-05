<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-05
  Time: 오후 5:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 재고 관리</title>
</head>
<body>

<h2>상품 추가/업데이트</h2>

<form action="AddQuantity" method="post">
    <label for="productName">상품 이름:</label><br>
    <input type="text" id="productName" name="productName"><br><br>
    <label for="quantity">추가 수량:</label><br>
    <input type="number" id="quantity" name="quantity"><br><br>
    <label for="productId">상품 번호:</label><br>
    <input type="number" id="productId" name="productId"><br><br>
    <input type="submit" value="추가하기">
</form>

</body>
</html>


