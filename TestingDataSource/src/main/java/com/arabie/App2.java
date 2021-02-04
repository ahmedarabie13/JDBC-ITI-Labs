package com.arabie;

import com.mysql.cj.jdbc.Driver;

import javax.sql.DataSource;
import java.sql.*;

public class App2 {
    public static void main(String[] args) {

        testDataSource();
    }
    private static void testDataSource() {

        DataSource ds = MyDataSourceFactory.getMYSQLDataSource();
        ResultSet rs ;
        try(Connection con = ds.getConnection();
            Statement stmt = con.createStatement())
        {
            rs = stmt.executeQuery("select * from city");
            while(rs.next()){
                System.out.print("City ID="+rs.getInt("city_id"));
                System.out.println(", City="+rs.getString("city"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}