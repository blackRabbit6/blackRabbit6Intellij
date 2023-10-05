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
            list-style-type: none; /*점 제거*/
            display:flex; /*flexbox 사용*/
            flex-direction: column;
            justify-content:center; /*가로 방향으로 중앙 정렬*/
        }
        ul li {
            transition: all 0.3s ease;
            padding: 10px;
            margin: 10px;
            background-color: skyblue; /* 배경색 */
            border-radius: 5px; /* 모서리 둥글게 */
        }

        ul li:hover {
            transform: translateY(-10px); /* 위로 이동 */
            box-shadow: 0 10px 20px black,
            0 6px 6px black /* 그림자 효과 */
        }

        ul li:active {
            transform: translateY(2px); /* 아래로 이동 */
            box-shadow: none; /* 그림자 제거 */
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
    <form action="UserMenu" method="post">
        <fieldset>
            <h1>WELCOME</h1>
            <h2>원하시는 기능을 선택하세요</h2>
            <ul>
                <li>
                    <input type="submit" name="action" value ="메뉴판 보기"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="장바구니 담기"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="장바구니 보기"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="추가"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="취소"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="구매"/>
                </li>
                <li>
                    <input type="submit" name="action" value ="종료"/>
                </li>
            </ul>
            <%--hidden, submit 둘다 쓴 이유: hidden(서버로 데이터 전송 목적),submit(누르면 form 데이터 서버로 전송)--%>
        </fieldset>
    </form>
</body>
</html>
