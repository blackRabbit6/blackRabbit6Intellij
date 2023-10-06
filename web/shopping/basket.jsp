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
    <style>
        /* 스타일: 컨테이너 div를 가운데 정렬하고, 필요에 따라 너비를 조정하세요. */
        .container {
            text-align: center;
            margin: 0 auto;
            width: 50%;
        }

        /* 스타일: fieldset 스타일 수정하여 선이 둥글게 표시됩니다. */
        fieldset {
            text-align: center;
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 10px;
        }

        /* 스타일: 메뉴명을 박스로 감싸고, 필요에 따라 스타일을 조정하세요. */
        .menu-box {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 5px;
            margin-bottom: 10px;
            cursor: pointer;
        }

        /* 스타일: 수량 입력 필드 및 추가 버튼을 초기에 숨깁니다. */
        .quantity-and-submit-button {
            display: none;
        }
    </style>

</head>
<body>
<div class="container">
    <!-- 여러 메뉴를 나열하는 fieldset -->
    <fieldset>
        <legend>장바구니</legend> <!-- '장바구니' 텍스트를 나타냅니다. -->

        <% List<MenuItem> menuList = (List<MenuItem>)request.getAttribute("menu");
            if(menuList != null) {
                for(MenuItem menuItem : menuList) {
        %>
        <form action="Basket" method="post">
            <!-- 메뉴명 표시 -->
            <div class="menu-box">
                <span id="<%=menuItem.getProductName()%>-name" onclick="toggleQuantityInputAndSubmitButton('<%=menuItem.getProductName()%>')"><%=menuItem.getProductName()%></span>

                <!-- 수량 입력 필드와 추가 버튼 -->
                <div class="quantity-and-submit-button" id="<%=menuItem.getProductName()%>-quantity-and-submit-button">
                    <!-- 수량 입력 필드 -->
                    <input type='number' name='quantity' min='1' placeholder='수량'>

                    <!-- 메뉴 이름을 form 데이터로 전송하기 위한 hidden input field -->
                    <input type='hidden' name='productname' value='<%=menuItem.getProductName()%>'/>

                    <!-- 장바구니에 추가 버튼 -->
                    <input type='submit' value='장바구니에 추가'>
                </div>
            </div>
        </form>

        <% } } %>
        <!-- 뒤로 가기 버튼 -->
        <input type="button" value="뒤로 가기" onclick="location.href='/shop_Web_exploded/shopping/userMenu.jsp'">
    </fieldset>

</div>


<!-- 메시지 출력 스크립트 -->
<script>
    var message = "<%= request.getAttribute("message") %>";
    if (typeof message === 'string' && message !== "" && message !== "null") {
        alert(message);
    }
</script>
<script type="text/javascript">
    function toggleQuantityInputAndSubmitButton(menu) {
        var quantityInputAndSubmitButton = document.getElementById(menu + "-quantity-and-submit-button");

        if(quantityInputAndSubmitButton.style.display === "none" || quantityInputAndSubmitButton.style.display === ""){
            // 메뉴명 클릭-수량입력, 추가버튼 표시
            quantityInputAndSubmitButton.style.display = "block";
        }else{
            // 메뉴명 클릭- 메뉴명만 나오게
            quantityInputAndSubmitButton.style.display = "none";
        }

    }
</script>
</body>
</html>

