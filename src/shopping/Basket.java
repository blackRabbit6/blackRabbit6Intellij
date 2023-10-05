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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/shopping/Basket")
public class Basket extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MenuItem> menuList = getMenu();
        req.setAttribute("menu",menuList);
        req.getRequestDispatcher("/shopping/basket.jsp").forward(req,resp);
    }

    private List<MenuItem> getMenu(){
        List<MenuItem> menuList = new ArrayList<>();
        Connection connection;

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection=DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);

            if(connection != null){
                String selectSql = "SELECT productname FROM MENU";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSql);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    MenuItem menuItem = new MenuItem(resultSet.getString("productname"));
                    menuList.add(menuItem);
                }
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return menuList;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String productName = req.getParameter("productname");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // 세션에서 사용자 정보 불러오기
        HttpSession session=req.getSession();
        User user =(User) session.getAttribute("user");

        if(user == null){
            resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
            return;
        }

        String id=user.getId();
        String name=user.getName();


        Connection connection=null;

        resp.setContentType("text/html;charset=UTF-8");

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection= DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);

            if(connection!=null){
                String stockSql="SELECT quantity FROM MENU where productname=?";
                PreparedStatement stockStatement=connection.prepareStatement(stockSql);
                stockStatement.setString(1,productName);

                ResultSet stockResult=stockStatement.executeQuery();

                if(stockResult.next()){
                    int currentStock=stockResult.getInt("quantity");
                    if(quantity<=0){
                        req.setAttribute("message","재고가 0입니다.");
                    }else if(quantity>currentStock){
                        req.setAttribute("message","입력하신 수량이 재고량 보다 많습니다.");
                    }else{
                        // basket 테이블에 사용자 ID와 이름도 함께 저장
                        String basketSql="INSERT INTO BASKET(productname, quantity, id, name) VALUES(?,?,?,?)";
                        PreparedStatement basketStatement=connection.prepareStatement(basketSql);
                        basketStatement.setString(1,productName);
                        basketStatement.setInt(2,quantity);
                        basketStatement.setString(3,id);
                        basketStatement.setString(4,name);

                        int result=basketStatement.executeUpdate();

                        if(result>0){
                            req.setAttribute("message","선택하신 "+productName+"를 "+quantity+" 수량을 장바구니에 담으셨습니다.");
                            resp.sendRedirect(req.getContextPath() + "/shopping/basketComplete.jsp");
                            return;
                        }
                    }
                }else{
                    req.setAttribute("message",productName+"이라는 이름을 찾을수 없습니다");
                }
            }

            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        resp.sendRedirect(req.getContextPath()+"/shopping/basket.jsp");

    }
}
