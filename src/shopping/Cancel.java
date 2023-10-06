package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/shopping/Cancel")
public class Cancel extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 세션에서 사용자 정보 불러오기
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
            return;
        }

        String id = user.getId();

        Connection connection = null;

        resp.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                // 사용자의 장바구니 비우기
                String clearBasketSql = "DELETE FROM BASKET WHERE id=?";
                PreparedStatement clearBasketStatement = connection.prepareStatement(clearBasketSql);
                clearBasketStatement.setString(1, id);

                int rowsAffected = clearBasketStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // 메시지 설정
                    req.setAttribute("message", "장바구니를 비웠습니다.");
                    req.getRequestDispatcher("/shopping/cancelComplete.jsp").forward(req, resp);
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
