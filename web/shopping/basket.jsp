<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-04
  Time: 오후 6:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="shopping.MenuItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>

    <script type="text/javascript">
        function showQuantityInputAndSubmitButton(menu) {
            var quantityInputAndSubmitButton = document.getElementById(menu + "-quantity-and-submit-button");
            quantityInputAndSubmitButton.style.display = "block";

            var addButton = document.getElementById(menu + "-add-button");
            addButton.disabled = true;
        }
    </script>

</head>
<body>

<% List<MenuItem> menuList = (List<MenuItem>)request.getAttribute("menu");
    if(menuList != null) {
        for(MenuItem menuItem : menuList) {
%>
<form action="Basket" method="post">
    <fieldset>
        <ul>
            <li><label for="<%=menuItem.getProductName()%>" class="title"><%=menuItem.getProductName()%></label></li>

            <!-- Initially hidden quantity input field and submit button -->
            <div id="<%=menuItem.getProductName()%>-quantity-and-submit-button" style="display: none;">
                <!-- 수량 입력 필드 -->
                <input type='number' name='quantity' min='1' placeholder='수량'>

                <!-- 메뉴 이름을 form 데이터로 전송하기 위한 hidden input field -->
                <input type='hidden' name='productname' value='<%=menuItem.getProductName()%>'/>

                <!-- 장바구니에 추가 버튼 -->
                <input type='submit' value='장바구니에 추가'>
            </div>

            <!-- '수량 입력 및 추가' 버튼; 클릭 시 수량 입력 필드와 submit button 표시-->
            <button id="<%=menuItem.getProductName()%>-add-button" type="button" onclick="showQuantityInputAndSubmitButton('<%=menuItem.getProductName()%>')">수량 입력 및 추가</button>
        </ul>
    </fieldset>
</form>

<% } } %>

<!-- 뒤로 가기 버튼 -->
<input type="button" value="뒤로 가기" onclick="location.href='/shop_Web_exploded/shopping/userMenu.jsp'">

<!-- 메시지 출력 스크립트 -->
<script>
    var message = "<%= request.getAttribute("message") %>";
    if (typeof message === 'string' && message !== "" && message !== "null") {
        alert(message);
    }
</script>

</body>
</html>

