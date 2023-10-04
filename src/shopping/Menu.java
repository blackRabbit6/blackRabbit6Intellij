package shopping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/shopping/Menu")
public class Menu extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

//    doGet(사용자가 그냥 데이터를 부르거나 특정요청할때 씀)
//    doPost(사용자가 직접 데이터를 수정 삭제 추가등 할때 씀)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Connection connection = null;
        List<MenuItem> menuList = new ArrayList<>();

        resp.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                // 쿼리문 -> 메뉴판 테이블에서 productname quantity 불러움
                String showSql = "SELECT productname, quantity FROM menu";
                PreparedStatement preparedStatement = connection.prepareStatement(showSql);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    MenuItem menuItem = new MenuItem(resultSet.getString("productname"), resultSet.getInt("quantity"));
                    menuList.add(menuItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 메뉴 리스트를 request 객체에 저장
        req.setAttribute("menuList", menuList);

        // JSP 페이지로 forward
        RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/menu.jsp");
        dispatcher.forward(req, resp);
    }
}
