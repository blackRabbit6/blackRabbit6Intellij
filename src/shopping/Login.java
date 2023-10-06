package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/shopping/Login")
public class Login extends HttpServlet {
    //private 쓰는이유: 클래스 내부에서만 접근 가능하게 제한하는 접근 제어자(외부에서 데이터베이스 접근 방지)
    // static= 클래스자체 속함, 한번만 생성, 모든인스턴스 공유하는변수 메소드 정의
    // final = 값 변동 막음
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String pw = req.getParameter("pw");
        Connection connection = null;

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if(connection != null){
                String checkSql = "SELECT 'USER' as type ,name FROM USERS WHERE id=? AND pw=?"+
                        " UNION ALL SELECT 'MANAGER' as type, name FROM MANAGER WHERE id=? AND pw=?" ;
                PreparedStatement preparedStatement = connection.prepareStatement(checkSql);
                preparedStatement.setString(1,id);
                preparedStatement.setString(2,pw);
                preparedStatement.setString(3,id);
                preparedStatement.setString(4,pw);
                // 1,2-> user 테이블 3,4-> manager테이블 쿼리문 받을려고 4개 사용함

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String userType = resultSet.getString("type");
                    String userName = resultSet.getString("name");

                    // 세션 생성 및 사용자 유형 저장
                    HttpSession session = req.getSession();
                    session.setAttribute("userType", userType.toLowerCase());  // "user" 또는 "manager"
                    if("USER".equals(userType)){
                        req.setAttribute("message", userName +" 님 환영합니다! 사용자 메뉴로 안내합니다!");
                        // 로그인 성공 시 User 객체 생성 및 세션에 저장
                        User user = new User(id, userName);
                        session.setAttribute("user", user);
                        req.getRequestDispatcher("/shopping/userMenu.jsp").forward(req, resp);
                    } else if ("MANAGER".equals(userType)) {
                        req.setAttribute("message", userName+" 님 어서오세요. 관리자메뉴로 이동합니다.");
                        req.getRequestDispatcher("/shopping/managerMenu.jsp").forward(req, resp);
                    }
                    return;
                }
                req.setAttribute("message","로그인 실패: ID 혹은 비밀번호가 잘못되었습니다.");
                req.getRequestDispatcher("/shopping/login.jsp").forward(req, resp);

                preparedStatement.close();
            }
        }catch (Exception e){
            req.setAttribute("message","오류발생: "+ e.getMessage());
        }
    }
}
