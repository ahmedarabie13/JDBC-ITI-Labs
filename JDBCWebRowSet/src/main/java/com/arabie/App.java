package com.arabie;

import com.mysql.cj.jdbc.Driver;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class App {
    public static void main(String[] args) throws SQLException, IOException {

        RowSetFactory myRSF = RowSetProvider.newFactory();
        var webRowSet = myRSF.createWebRowSet();

        webRowSet.setUrl("jdbc:mysql://localhost:3306/myschema" );
        webRowSet.setUsername("root");
        webRowSet.setPassword("1420");
        webRowSet.setCommand("select * from emp");
        webRowSet.execute();

        System.out.println("-----------Using RowSet------------\n");
        while (webRowSet.next()){
            System.out.print("ID "+webRowSet.getString(1));
            System.out.println(" Name "+webRowSet.getString(2)+" sal "+webRowSet.getString(3));
        }
///load data from xml
        FileOutputStream fileOutputStream = new FileOutputStream("xml.xml");
        webRowSet.writeXml(fileOutputStream);

        webRowSet.close();


    }
}