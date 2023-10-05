package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shopping/UserMenu")
public class UserMenu extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if("메뉴판 보기".equals(action)){
            resp.sendRedirect(req.getContextPath()+"/shopping/Menu");
        } else if ("장바구니 담기".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/Basket");
        } else if ("장바구니 보기".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/look.jsp");
        }else if ("추가".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/add.jsp");
        }else if ("취소".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/cancel.jsp");
        }else if ("구매".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/purchase.jsp");
        }else if ("종료".equals(action)) {
            // 로그아웃 메시지를 세션에 저장
            req.getSession().setAttribute("message", "로그아웃 되었습니다.");
            // 초기 화면으로 리다이렉트
            resp.sendRedirect(req.getContextPath() + "/shopping/start.jsp");
        }
    }
}
