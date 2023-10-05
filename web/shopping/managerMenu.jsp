<%--
  Created by IntelliJ IDEA.
  User: LG
  Date: 2023-10-03
  Time: 오후 4:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>메뉴창</title>
    <script>
        var message = '<%= request.getAttribute("message") %>';
        // 메시지가 있는 경우만 알림창 띄우기
        if (message && message !== 'null') {
            alert(message);
        }
    </script>
    <style>
        fieldset{
            border-radius: 10px;
            text-align: center;
            width: fit-content;
            margin-left: auto;
            margin-right: auto;
        }
        h1 h2{
            text-align: center;
        }
        ul{
            list-style-type: none;
            display:flex;
            flex-direction: column;
            justify-content:center;
        }
        ul li {
            transition: all 0.3s ease;
            padding: 10px;
            margin: 10px;
            background-color: skyblue;
            border-radius: 5px;
        }

        ul li:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 20px black,
            0 6px 6px black
        }

        ul li:active {
            transform: translateY(2px);
            box-shadow: none;
        }
        li{
            text-align: center;
        }
        input[type=submit] {
            background: none;
            color: inherit;
            border: none;
            padding: 0;
            font: inherit;
            cursor: pointer;
            outline: inherit;
        }
    </style>
</head>
<body>
<form action="ManagerMenu" method="post">
    <fieldset>
        <h1>관리자 전용 메뉴</h1>
        <h2>원하시는 기능을 선택하세요</h2>
        <ul>
            <%--잘 안된 이유: type명이 두개가 있기 때문에 에러날 확률 높음 그래서하나만 쓰기! --%>
            <li>
                <input type="submit" name="action" value ="메뉴판 보기"/>
            </li>
            <li>
                <input type="submit" name="action" value ="재고수량 추가"/>
            </li>
            <li>
                <input type="submit" name="action" value ="구매내역 보기"/>
            </li>
            <li>
                <input type="submit" name="action" value ="관리자 추가"/>
            </li>
            <li>
                <input type="submit" name="action" value ="종료"/>
            </li>
        </ul>
    </fieldset>
</form>
</body>
</html>
