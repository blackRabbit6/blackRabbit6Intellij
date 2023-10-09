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

@WebServlet("/shopping/SearchProduct")
public class SearchProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String productName = request.getParameter("productName");

        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.86:1521:XE", "shop", "shop");

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
