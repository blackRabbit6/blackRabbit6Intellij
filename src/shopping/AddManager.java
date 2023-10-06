package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/shopping/AddManager")
public class AddManager extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String pw = req.getParameter("pw");
        String name = req.getParameter("name");
        Connection connection = null;

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if(connection != null){

                String checkSql = "SELECT COUNT(*) FROM (SELECT id FROM USERS UNION SELECT id FROM MANAGER) WHERE id=?";
                PreparedStatement checkstatement = connection.prepareStatement(checkSql);
                checkstatement.setString(1,id);
                ResultSet resultset = checkstatement.executeQuery();

                if(resultset.next() && resultset.getInt(1)>0){
                    req.setAttribute("message","입력하신 ID는 사용중입니다.");
                    req.getRequestDispatcher("/shopping/addManager.jsp").forward(req, resp);
                }else {

                    String insertSql = "INSERT INTO MANAGER(id, pw, name) VALUES (?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, pw);
                    preparedStatement.setString(3, name);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        req.setAttribute("message", "새로운 관리자가 추가되었습니다.");
                        //start로 돌아감
                        req.getRequestDispatcher("/shopping/complete.jsp").forward(req, resp);
                    } else {
                        req.setAttribute("message", "관리자 추가 실패하셨습니다.");
                        //HttpServletRequest.getRequestDispatcher() 메서드는 상대 경로를 사용
                        req.getRequestDispatcher("/shopping/addManager.jsp").forward(req, resp);
                    }


                    preparedStatement.close();
                }
                checkstatement.close();
                connection.close();
            }
        }catch (Exception e){
            req.setAttribute("message", "오류발생: " + e.getMessage());
        }
    }
}
