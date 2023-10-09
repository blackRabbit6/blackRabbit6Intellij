<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-06
  Time: 오후 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>재고 추가 완료</title>
    <style>
        fieldset{
            border-radius: 10px;
            text-align: center;
            width: fit-content;
            margin-left: auto;
            margin-right: auto;
        }
        h1,h3,input{
            text-align: center;
        }
        input[type="submit"] {
            margin: 10px;
            padding: 10px 20px;
            border: none;
            background-color: lightgreen;
            color: black;
            border-radius: 10px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover{
            background-color: lightcoral;
            color: black;
        }
    </style>
</head>
<body>
<fieldset>
    <h1>재고 추가 완료!!</h1>
    <h3>관리자화면으로 돌아가세요</h3>
    <input type="submit" value="관리자화면 돌아가기" onclick="location.href='/shop_Web_exploded/shopping/managerMenu.jsp'">
</fieldset>
</body>
</html>
