package com.arabie;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        String mySQLUr0l ="jdbc:mysql://localhost:3306/sakila";
        String user = "root";
        String pwd = "1420";

        try(Connection connection =DriverManager.getConnection(mySQLUr0l,user,pwd);
            Statement stmt = connection.createStatement();){

        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        String query="select * from actor;";
            ResultSet resultSet =stmt.executeQuery(query);
            while (resultSet.next()){
                System.out.print(resultSet.getString(1)+"  "+resultSet.getString(2)+"\n");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}