package login.servlet;

import login.controller.Controller;
import login.controller.ControllerImpl;
import login.dto.Dto;
import login.entity.Entity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {
    private final Controller controller = new ControllerImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String pw = req.getParameter("pw");

        Dto.LoginRequestDTO requestDTO = new Dto.LoginRequestDTO(id, pw);

        Object result = controller.handleLogin(requestDTO);

        if(result instanceof Entity.User){
            Entity.User user = (Entity.User) result;
            req.setAttribute("message", user.getName() + " 님 환영합니다! 사용자 메뉴로 안내합니다!");
            req.getSession().setAttribute("user", user);
            req.getRequestDispatcher("/Window/userMenu.jsp").forward(req, resp);
        }else if (result instanceof Entity.Manager) {
            Entity.Manager manager = (Entity.Manager) result;
            req.setAttribute("message", manager.getName() + " 님 어서오세요. 관리자메뉴로 이동합니다.");
            req.getRequestDispatcher("/Window/managerMenu.jsp").forward(req, resp);

        } else {
            req.setAttribute("message","로그인 실패: ID 혹은 비밀번호가 잘못되었습니다.");
            req.getRequestDispatcher("/Login/login.jsp").forward(req, resp);
        }
    }
}
