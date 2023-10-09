<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 재고 관리</title>
    <style>
        fieldset {
            border-radius: 10px;
            text-align: center;
            width: fit-content;
            margin-left: auto;
            margin-right: auto;
        }

        h2, input {
            text-align: center;
        }

        input[type='button'],input[type="submit"] {
            padding: 10px 20px;
            border: none;
            background-color: beige;
            color: black;
            border-radius: 10px;
            cursor: pointer;
            font-size: 17px;
            margin-left: auto;
            margin-right: auto;
        }

        input[type='button'], input[type="submit"]:hover {
            background-color: lightgreen;
            color: black;
        }
    </style>


</head>

<body>

<fieldset>

    <h2>상품 추가/업데이트</h2>

    <!-- 상품 검색 폼 -->
    <form id="productSearchForm" action="/shop_Web_exploded/shopping/SearchProduct" method="get">
        <div id='productNameBox'>
            <label for='productName'>상품 이름</label><br/>
            <input type='text' id='productName' name='productName'><br/><br/>
        </div>

        <!-- 검색 버튼 -->
        <input type='button' value='검색하기' onclick='searchProduct()' id='searchButton'><br/><br/>

        <!-- 수량 추가 입력 창 -->
        <div id ='quantityBox' style='display:none;'>
            <label for='quantityInput'>추가 수량</label><br/>
            <input type='number' id='quantityInput' name='quantity' min='0'><br/><br/>

            <!-- 제품 ID 입력 창 -->
            <div id ='productIdBox' style='display:none;'>
                <label for='productIdInput'>제품 번호</label><br/>
                <input type='number' id='productIdInput' name='productId' min='0' oninput='checkProductId()'>
                <span id='warningMessage'></span><br/>
            </div>

            <!-- 추가 버튼 -->
            <input type='button' value='추가하기' id='addButtonWithId' onclick='addQuantity()' style='display:none;'>
        </div>
    </form>


</fieldset>

<script>
    function searchProduct() {
        var productName = document.getElementById("productName").value;

        if (productName.trim() === "") {
            alert("상품 이름을 입력하세요.");
            return;
        }

        // AJAX 요청으로 서버에 제품명을 전달하여 검색 결과를 받아옴
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/shop_Web_exploded/shopping/SearchProduct?productName=" + encodeURIComponent(productName), true);

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var responseText = xhr.responseText.trim();

                    // 검색 결과에 따라 화면 요소를 동적으로 변경
                    if (responseText === "existing") {
                        document.getElementById('searchButton').style.display = "none";
                        document.getElementById('quantityBox').style.display = "block";
                        document.getElementById('productIdBox').style.display = "none"; // 제품 ID 입력 창 숨김
                        document.getElementById('addButtonWithId').style.display = "block"; // 추가 버튼 표시
                    } else if (responseText === "not_existing") {
                        document.getElementById('searchButton').style.display = "none";
                        document.getElementById('quantityBox').style.display = "block";
                        document.getElementById('productIdBox').style.display = "block"; // 제품 ID 입력 창 표시
                        document.getElementById('addButtonWithId').style.display = "block"; // 추가 버튼 표시
                    } else {
                        document.getElementById('addButtonWithId').style.display = "none"; // 제품명이 메뉴 테이블에도 없고 다른 오류일 경우 추가 버튼 감춤
                    }

                } else {
                    console.log('Error:', xhr.status);
                }
            }
        };
        xhr.send();
    }

    function addQuantity() {
        var productName = document.getElementById("productName").value;
        var quantity = document.getElementById("quantityInput").value;
        var productId = document.getElementById("productIdInput").value;

        if (productName.trim() === "") {
            alert("상품 이름을 입력하세요.");
            return;
        }

        // AJAX 요청으로 서버에 제품 정보를 전달하여 업데이트 또는 추가
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/shop_Web_exploded/shopping/AddQuantity", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        var data = "productName=" + encodeURIComponent(productName) + "&quantity=" + quantity + "&productId=" + productId;

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var responseText = xhr.responseText.trim();

                    // 업데이트 또는 추가가 성공한 경우
                    if (responseText === "success") {
                        alert("업데이트 또는 추가가 성공하였습니다.");
                        window.location.href = "/shop_Web_exploded/shopping/addComplete.jsp";
                    } else if (responseText.startsWith("DuplicateProductId")) {
                        // 제품 ID가 이미 존재하는 경우
                        var existingProductId = responseText.split(":")[1]; // 중복된 제품 ID 추출
                        alert("이미 존재하는 제품 ID입니다: " + existingProductId);
                    } else {
                        var textMessage = responseText.replace(/<\/?[^>]+(>|$)/g, ""); // HTML 태그 제거
                        alert(textMessage);
                    }
                } else {
                    console.log('Error:', xhr.status);
                }
            }
        };
        xhr.send(data);
    }

</script>

</body>
</html>
