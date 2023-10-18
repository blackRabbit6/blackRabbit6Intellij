package login.dao;

import login.dto.Dto;
import login.entity.Entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DaoImpl implements Dao {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String JDBC_USER = "shop";
    private static final String JDBC_PASSWORD = "shop";

    @Override
    public Entity.User findUserByIdAndPw(Dto.LoginRequestDTO loginRequest) {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String sql ="SELECT * FROM users WHERE id=? AND pw=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, loginRequest.getId());
            ps.setString(2, loginRequest.getPw());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Entity.User(rs.getString("id"),rs.getString("pw"), rs.getString("name"));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(connection != null){
                try{
                    connection.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Entity.Manager findManagerByIdAndPw(Dto.LoginRequestDTO loginRequest) {
        Connection connection = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String sql ="SELECT * FROM manager WHERE id=? AND pw=?";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,loginRequest.getId());
            ps.setString(2,loginRequest.getPw());

            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                return new Entity.Manager(rs.getString("id"),rs.getString("pw"), rs.getString("name"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (connection != null) {
                try{
                    connection.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        return null;  // 일치하는 관리자 없음
    }
}
