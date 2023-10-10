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
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
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
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
            return;
        }

        String id = user.getId();
        String name = user.getName();

        Connection connection = null;

        resp.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                // 장바구니에 이미 존재하는지 확인
                String existingBasketSql = "SELECT quantity FROM BASKET WHERE productname=? AND id=?";
                PreparedStatement existingBasketStatement = connection.prepareStatement(existingBasketSql);
                existingBasketStatement.setString(1, productName);
                existingBasketStatement.setString(2, id);

                ResultSet existingBasketResult = existingBasketStatement.executeQuery();

                if (existingBasketResult.next()) {
                    int currentQuantity = existingBasketResult.getInt("quantity");
                    int newQuantity = currentQuantity + quantity;

                    String stockSql = "SELECT quantity FROM MENU where productname=?";
                    PreparedStatement stockStatement = connection.prepareStatement(stockSql);
                    stockStatement.setString(1, productName);

                    ResultSet stockResult = stockStatement.executeQuery();

                    if (stockResult.next()) {
                        int currentStock = stockResult.getInt("quantity");

                        if (newQuantity > currentStock) {
                            // 재고량을 초과하는 수량을 입력한 경우 알림 표시 및 장바구니로 돌아가기
                            req.setAttribute("message", "원하시는 제품의 수량의 재고량이 품절이거나, 재고량이 적으니 다시 확인후, 구매 진행해주세요.");

                            // 수정된 부분: 바로 장바구니.jsp로 포워딩
                            List<MenuItem> menuList = getMenu();
                            req.setAttribute("menu", menuList);
                            req.getRequestDispatcher("/shopping/basket.jsp").forward(req, resp);
                        } else if (newQuantity >= 0) {
                            // 장바구니 수량 업데이트
                            String updateBasketSql = "UPDATE BASKET SET quantity=? WHERE productname=? AND id=?";
                            PreparedStatement updateBasketStatement = connection.prepareStatement(updateBasketSql);
                            updateBasketStatement.setInt(1, newQuantity);
                            updateBasketStatement.setString(2, productName);
                            updateBasketStatement.setString(3, id);
                            updateBasketStatement.executeUpdate();

                            // 메시지 설정
                            req.setAttribute("message", productName + "의 수량을 " + quantity + " 개를 추가로 장바구니에 담았습니다.");
                            req.getRequestDispatcher("/shopping/basketComplete.jsp").forward(req, resp);
                        } else {
                            // 수량이 0보다 작으면 알림 표시
                            req.setAttribute("message", "수량은 0 이상이어야 합니다.");

                            // 수정된 부분: 바로 장바구니.jsp로 포워딩
                            List<MenuItem> menuList = getMenu();
                            req.setAttribute("menu", menuList);
                            req.getRequestDispatcher("/shopping/basket.jsp").forward(req, resp);
                        }
                    }
                }  else {
                    // 장바구니에 없는 경우 추가
                    String stockSql = "SELECT quantity FROM MENU where productname=?";
                    PreparedStatement stockStatement = connection.prepareStatement(stockSql);
                    stockStatement.setString(1, productName);

                    ResultSet stockResult = stockStatement.executeQuery();

                    if (stockResult.next()) {
                        int currentStock = stockResult.getInt("quantity");
                        if (quantity <= 0 || quantity > currentStock) {
                            // 재고가 0이거나 입력한 수량이 재고량보다 많을 때 알림 표시
                            req.setAttribute("message", "원하시는 제품의 수량의 재고량이 품절이거나, 재고량이 적으니 다시 확인후, 구매 진행해주세요.");

                            // 수정된 부분: 바로 장바구니.jsp로 포워딩
                            List<MenuItem> menuList = getMenu();
                            req.setAttribute("menu", menuList);
                            req.getRequestDispatcher("/shopping/basket.jsp").forward(req, resp);
                        } else {
                            // basket 테이블에 사용자 ID와 이름도 함께 저장
                            String basketSql = "INSERT INTO BASKET(productname, quantity, id, name) VALUES(?,?,?,?)";
                            PreparedStatement basketStatement = connection.prepareStatement(basketSql);
                            basketStatement.setString(1, productName);
                            basketStatement.setInt(2, quantity);
                            basketStatement.setString(3, id);
                            basketStatement.setString(4, name);

                            int result = basketStatement.executeUpdate();

                            if (result > 0) {
                                // 메시지 설정
                                req.setAttribute("message", productName + "를 " + quantity + " 개를 장바구니에 담으셨습니다.");
                                req.getRequestDispatcher("/shopping/basketComplete.jsp").forward(req, resp);
                            }
                        }
                    } else {
                        req.setAttribute("message", productName + "이라는 이름을 찾을 수 없습니다.");

                        // 수정된 부분: 바로 장바구니.jsp로 포워딩
                        List<MenuItem> menuList = getMenu();
                        req.setAttribute("menu", menuList);
                        req.getRequestDispatcher("/shopping/basket.jsp").forward(req, resp);
                    }
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
