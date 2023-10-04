<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="shopping.MenuItem" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>메뉴판</title>
    <style>
        /* 페이지 전체를 가운데 정렬 */
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f5f5f5; /* 전체 배경색상 */
        }

        fieldset {
            background-color: #ffe4b5; /* fieldset 배경색상 */
            border-radius: 10px;
            text-align: center;
            width: 60%; /* fieldset 너비 조정 */
            margin: 0 auto; /* fieldset 가운데 정렬 */
        }

        /* 테이블 스타일 수정 */
        table {
            border-collapse: collapse;
            width: 60%; /* 테이블의 폭을 조정 */
            margin: 0 auto; /* 가운데 정렬 */
            background-color: #fff; /* 테이블 배경색상 */
        }

        th, td {
            padding: 5px;
            text-align: center;
            border: 1px solid #ccc; /* 테두리 스타일 */
        }

        th {
            background-color: saddlebrown; /* 헤더 배경색 */
            color: white; /* 헤더 텍스트 색상 */
        }
    </style>
</head>
<body>
    <fieldset>
        <h2>Menu</h2>
        <% List<MenuItem> menuList = (List<MenuItem>) request.getAttribute("menuList"); %>

        <% if (menuList != null && !menuList.isEmpty()) { %>

        <table border="1">
            <tr>
                <th>제품명</th>
                <th>수량</th>
            </tr>

            <% for (MenuItem menuItem : menuList) { %>
            <tr>
                <td><%= menuItem.getProductName() %>
                </td>
                <td><%= menuItem.getQuantity() %>
                </td>
            </tr>
            <% } %>
        </table>

        <% } else { %>
        <p>메뉴 데이터를 가져올 수 없습니다.</p>
        <p>Error details: <%= request.getAttribute("errorMessage") %>
        </p>
        <% } %>
    </fieldset>
</body>
</html>
