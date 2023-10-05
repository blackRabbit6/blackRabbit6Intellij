<%@ page import="java.util.List" %>
<%@ page import="shopping.MenuItem" %>
<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-04
  Time: 오후 6:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>
</head>
<body>

<form action="Basket" method="post">
    <fieldset>

        <%
            List<MenuItem> menuList = (List<MenuItem>)request.getAttribute("menu");
            if(menuList != null) {
                for(MenuItem menuItem : menuList) {
        %>
        <ul>
            <li>
                <label for="<%=menuItem.getProductName()%>" class="title">
                    <%=menuItem.getProductName()%>
                </label>
            </li>
            <!-- Initially hidden quantity input field and submit button -->
            <div id="<%=menuItem.getProductName()%>-quantity-and-submit-button" style="display: none;">
                <!-- 수량 입력 필드의 ID 속성 설정 -->
                <input type='number' id='<%=menuItem.getProductName()%>' name='quantity' min='1' placeholder='수량'>

                <!-- 장바구니에 추가 버튼 -->
                <input type='submit' value='장바구니에 추가'>

                <!-- 메뉴 이름을 form 데이터로 전송하기 위한 hidden input field -->
                <input type='hidden' name='productname' value='<%=menuItem.getProductName()%>'/>
            </div>

            <!-- '수량 입력 및 추가' 버튼; 클릭 시 수량 입력 필드와 submit button 표시-->
            <button type="button" onclick="showQuantityInputAndSubmitButton('<%=menuItem.getProductName()%>')">수량 입력 및 추가</button>
        </ul>
        <%
                }
            }
        %>
        <!-- 뒤로 가기 버튼 -->
        <input type="button" value="뒤로 가기" onclick="location.href='/shop_Web_exploded/shopping/userMenu.jsp'">

    </fieldset>
</form>


<!-- 메시지 출력 스크립트 -->
<script>
    var message = "<%= request.getAttribute("message") %>";
    if (typeof message === 'string' && message !== "" && message !== "null") {
        alert(message);
    }
    function showQuantityInputAndSubmitButton(menu) {
        // Get the corresponding quantity input field and submit button by id
        var quantityInputAndSubmitButton = document.getElementById(menu + "-quantity-and-submit-button");

        // Show the quantity input field and submit button
        quantityInputAndSubmitButton.style.display = "block";

        // Submit the form directly
        quantityInputAndSubmitButton.getElementsByTagName('form')[0].submit();
    }
</script>


</body>
</html>

