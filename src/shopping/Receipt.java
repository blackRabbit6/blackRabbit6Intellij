package shopping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/shopping/Receipt")
public class Receipt extends HttpServlet {
    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.0.86:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Connection connection = null;

        List<ReceiptItem> receiptList = getReceipt();

        req.setAttribute("receiptList", receiptList);
        req.getRequestDispatcher("/shopping/receipt.jsp").forward(req, resp);
    }

    private List<ReceiptItem> getReceipt() {
        List<ReceiptItem> receiptList = new ArrayList<>();
        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            if (connection != null) {
                String selectSql = "SELECT NAME, PRODUCTNAME, QUANTITY, BUYDAY, PURCHASENUM FROM RECEIPT";
                PreparedStatement preparedStatement = connection.prepareStatement(selectSql);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    String productName = resultSet.getString("PRODUCTNAME");
                    int quantity = resultSet.getInt("QUANTITY");
                    Date buyday = resultSet.getDate("BUYDAY");
                    int purchaseNum = resultSet.getInt("PURCHASENUM");

                    ReceiptItem receiptItem =
                            new ReceiptItem(name,
                                    productName,
                                    quantity,
                                    buyday,
                                    purchaseNum);

                    receiptList.add(receiptItem);
                }
            }

            return receiptList;

        } catch (Exception e) {
            e.printStackTrace();

            return null; // In case of an error return null

        } finally {

            // Always close the database connection here
            if(connection != null){
                try{
                    connection.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

        }
    }
}
