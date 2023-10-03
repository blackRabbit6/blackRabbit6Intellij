package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shopping/Start")
public class Start extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        // 서블릿 dopost 메서드 호출 확인 및 어떤 action 값 전달했는지 확인 하기 위한코드
        // 이거 회원가입으로 계속 안넘어가져서 3~4시간동안 안되서 물어본 코드임
        System.out.println("Start servlet doPost called");
        System.out.println("Action: " + action);

        if("로그인".equals(action)){
            resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
        } else if ("회원가입".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/shopping/register.jsp");
        }
    }
}


