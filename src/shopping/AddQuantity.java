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
        String productName = request.getParameter("productName");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int productId = Integer.parseInt(request.getParameter("productId"));

        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.86:1521:XE","shop", "shop");

            PreparedStatement pstSelect = conn.prepareStatement("SELECT * FROM MENU WHERE PRODUCTNAME=?");
            pstSelect.setString(1, productName);

            ResultSet rs = pstSelect.executeQuery();

            if(rs.next()) {
                int existingQuantity = rs.getInt("QUANTITY");
                quantity += existingQuantity;

                PreparedStatement pstUpdate = conn.prepareStatement("UPDATE MENU SET QUANTITY=? WHERE PRODUCTNAME=?");
                pstUpdate.setInt(1, quantity);
                pstUpdate.setString(2, productName);

                pstUpdate.executeUpdate();

            } else {

                PreparedStatement pstInsert = conn.prepareStatement(
                        "INSERT INTO MENU(PRODUCTNAME, QUANTITY, PRODUCTID) VALUES (?, ?, ?)");

                pstInsert.setString(1, productName);
                pstInsert.setInt(2, quantity);
                pstInsert.setInt(3, productId);

                pstInsert.executeUpdate();
            }

            // After processing the form data and updating the database,
            // redirect to ManagerMenu.jsp page.
            response.sendRedirect("/shop_Web_exploded/shopping/Login");

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(conn != null)
                    conn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
