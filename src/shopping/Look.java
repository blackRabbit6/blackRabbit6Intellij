package shopping;

import javax.servlet.RequestDispatcher;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/shopping/Look")
public class Look extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Connection connection = null;

        // basketList 생성 위치 변경
        List<MenuItem> basketList = new ArrayList<>();

        resp.setContentType("text/html;charset=UTF-8");

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if(connection != null){
                HttpSession session = req.getSession();
                User user = (User) session.getAttribute("user");

                // 로그인 상태 확인
                if (user == null) {
                    resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
                    return;
                }

                String userId = user.getId();
                String userName = user.getName();

                String showSql = "SELECT productname, quantity FROM BASKET WHERE id=? AND name=?";

                PreparedStatement showStatement = connection.prepareStatement(showSql);

                showStatement.setString(1,userId);
                showStatement.setString(2,userName);

                ResultSet resultSet = showStatement.executeQuery();

                while (resultSet.next()){
                    MenuItem menuItem = new MenuItem(resultSet.getString("productname"), resultSet.getInt("quantity"));
                    basketList.add(menuItem);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            req.setAttribute("errorMessage",e.toString());
        }finally {
            if(connection != null){
                try{
                    connection.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        req.setAttribute("basketList", basketList);

        RequestDispatcher dispatcher=req.getRequestDispatcher("/shopping/look.jsp");
        dispatcher.forward(req,resp);
    }
}
