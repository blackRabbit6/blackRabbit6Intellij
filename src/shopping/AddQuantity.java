package shopping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/shopping/AddQuantity")
public class AddQuantity extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String productName = request.getParameter("productName");

        // Check if the parameters are not empty before parsing them to integer
        String quantityParam = request.getParameter("quantity");
        int quantity = 0;

        if (quantityParam != null && !quantityParam.isEmpty()) {
            quantity = Integer.parseInt(quantityParam);
        }

        String productIdParam = request.getParameter("productId");
        int productId = 0;

        if (productIdParam != null && !productIdParam.isEmpty()) {
            productId = Integer.parseInt(productIdParam);
        }

        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.86:1521:XE", "shop", "shop");

            PreparedStatement pstSelect = conn.prepareStatement("SELECT * FROM MENU WHERE PRODUCTNAME=?");
            pstSelect.setString(1, productName);

            ResultSet rs = pstSelect.executeQuery();

            if (rs.next()) {
                int existingQuantity = rs.getInt("QUANTITY");
                quantity += existingQuantity;

                PreparedStatement pstUpdate = conn.prepareStatement("UPDATE MENU SET QUANTITY=? WHERE PRODUCTNAME=?");

                pstUpdate.setInt(1, quantity);
                pstUpdate.setString(2, productName);

                pstUpdate.executeUpdate();

                // 업데이트가 성공한 경우 "success"를 반환
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("success");
            } else {
                if (productId > 0) {
                    PreparedStatement pstCheckId = conn.prepareStatement("SELECT * FROM MENU WHERE PRODUCTID=?");

                    pstCheckId.setInt(1, productId);

                    ResultSet rsIdCheck = pstCheckId.executeQuery();

                    if (rsIdCheck.next()) {
                        response.getWriter().println("<p>이미 존재하는 제품 ID입니다.</p>");
                        return;
                    }

                    PreparedStatement pstInsert = conn.prepareStatement(
                            "INSERT INTO MENU(PRODUCTNAME, QUANTITY, PRODUCTID) VALUES (?, ?, ?)");

                    pstInsert.setString(1, productName);
                    pstInsert.setInt(2, quantity);
                    pstInsert.setInt(3, productId);

                    pstInsert.executeUpdate();

                    // 추가가 성공한 경우 "success"를 반환
                    response.setContentType("text/plain;charset=UTF-8");
                    response.getWriter().write("success");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String productName = request.getParameter("productName");

        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "shop", "shop");

            PreparedStatement pstSelect = conn.prepareStatement("SELECT * FROM MENU WHERE PRODUCTNAME=?");
            pstSelect.setString(1, productName);

            ResultSet rsSelectProductByName = pstSelect.executeQuery();

            if (rsSelectProductByName.next()) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("existing");
            } else {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("not_existing");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
