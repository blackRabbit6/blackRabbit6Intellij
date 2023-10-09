package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shopping/ManagerMenu")
public class ManagerMenu extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if("메뉴판 보기".equals(action)){
            resp.sendRedirect(req.getContextPath()+"/shopping/Menu");
        } else if ("재고수량 추가".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/addQuantity.jsp");
        } else if ("구매내역 보기".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/Receipt");
        }else if ("관리자 추가".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/addManager.jsp");
        }else if ("종료".equals(action)) {
            // 로그아웃 메시지를 세션에 저장
            req.getSession().setAttribute("message", "로그아웃 되었습니다.");
            // 초기 화면으로 리다이렉트
            resp.sendRedirect(req.getContextPath() + "/shopping/start.jsp");
        }
    }
}
