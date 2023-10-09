package shopping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/shopping/Purchase")
public class Purchase extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Connection connection = null;
        List<MenuItem> basketList = new ArrayList<>();

        resp.setContentType("text/html;charset=UTF-8");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                HttpSession session = req.getSession();
                User user = (User) session.getAttribute("user");

                if (user == null) {
                    resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
                    return;
                }

                String userId = user.getId();
                String userName = user.getName();

                String showSql = "SELECT productname, quantity FROM BASKET WHERE id=? AND name=?";

                PreparedStatement showStatement = connection.prepareStatement(showSql);

                showStatement.setString(1, userId);
                showStatement.setString(2, userName);

                ResultSet resultSet = showStatement.executeQuery();

                while (resultSet.next()) {
                    MenuItem menuItem = new MenuItem(resultSet.getString("productname"), resultSet.getInt("quantity"));
                    basketList.add(menuItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        req.setAttribute("basketList", basketList);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/shopping/login.jsp");
            return;
        }

        String userId = user.getId();
        String userName = user.getName();

        String[] basketList = req.getParameterValues("basketList"); // 변수명 변경

        if (basketList == null || basketList.length == 0) { // 변수명 변경
            // 체크된 상품이 없을 때의 처리
            req.setAttribute("errorMessage", "선택된 상품이 없습니다.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            boolean cartIsEmpty = true;

            for (String item : basketList) { // 변수명 변경
                String[] itemDetails = item.split(",");

                String productName = itemDetails[0];
                int quantityToBuy = Integer.parseInt(itemDetails[1]);

                PreparedStatement checkStmt = connection.prepareStatement("SELECT quantity FROM MENU WHERE productname=?");
                checkStmt.setString(1, productName);

                ResultSet rsCheckStock = checkStmt.executeQuery();

                if (rsCheckStock.next()) {
                    int stockQuantity = rsCheckStock.getInt(1);

                    if (stockQuantity <= 0) { // 수정된 부분
                        req.setAttribute("errorMessage", "품절입니다. 다음에 구매 해주세요.");
                        RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
                        dispatcher.forward(req, resp);
                        return;
                    }

                    if (stockQuantity < quantityToBuy) {
                        req.setAttribute("errorMessage", "재고가 부족합니다. 다시 주문 부탁드립니다.");
                        RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
                        dispatcher.forward(req, resp);
                        return;
                    }

                    PreparedStatement updateMenu = connection.prepareStatement("UPDATE MENU SET quantity=quantity-? WHERE productname=? AND quantity >= ?");
                    updateMenu.setInt(1, quantityToBuy);
                    updateMenu.setString(2, productName);
                    updateMenu.setInt(3, quantityToBuy);
                    int rowsUpdated = updateMenu.executeUpdate();

                    if (rowsUpdated == 0) {
                        req.setAttribute("errorMessage", "재고가 부족합니다. 다시 주문 부탁드립니다.");
                        RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
                        dispatcher.forward(req, resp);
                        return;
                    }

                    PreparedStatement insertReceipt = connection.prepareStatement(
                            "INSERT INTO RECEIPT (id,name,productname,quantity,buyday,purchasenum) VALUES (?,?,?,?,SYSDATE,SEQUENCE1.NEXTVAL)");
                    insertReceipt.setString(1, userId);
                    insertReceipt.setString(2, userName);
                    insertReceipt.setString(3, productName);
                    insertReceipt.setInt(4, quantityToBuy);
                    insertReceipt.executeUpdate();

                    cartIsEmpty = false;
                } else {
                    req.setAttribute("errorMessage", "해당 제품을 찾을 수 없습니다.");
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
                    dispatcher.forward(req, resp);
                    return;
                }
            }

            if (!cartIsEmpty) {
                PreparedStatement deleteBasket = connection.prepareStatement("DELETE FROM BASKET WHERE id=? AND name=? AND productname=?");

                for (String item : basketList) { // 변수명 변경
                    String[] itemDetails = item.split(",");
                    deleteBasket.setString(1, user.getId());
                    deleteBasket.setString(2, user.getName());
                    deleteBasket.setString(3, itemDetails[0]);
                    deleteBasket.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.toString());

            RequestDispatcher dispatcher = req.getRequestDispatcher("/shopping/purchase.jsp");
            dispatcher.forward(req, resp);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/shopping/purchaseComplete.jsp");
    }
}
