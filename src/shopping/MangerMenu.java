package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/shopping/MangerMenu")
public class MangerMenu extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if("메뉴판보기".equals(action)){
            resp.sendRedirect(req.getContextPath()+"/shopping/menu.jsp");
        } else if ("재고수량 추가".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/addQuantity.jsp");
        } else if ("구매내역 보기".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/receipt.jsp");
        }else if ("새 관리자 추가".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/addManager.jsp");
        }else if ("종료".equals(action)) {
            resp.sendRedirect(req.getContextPath()+"/shopping/logout.jsp");
        }
    }
}
