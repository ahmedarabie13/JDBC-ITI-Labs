package com.arabie;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class App {

    public static void main(String[] args) throws SQLException {

        //auto commit enabled by default

        RowSetFactory myRSF = RowSetProvider.newFactory();
        var jdbcRs = myRSF.createJdbcRowSet();
        jdbcRs.setUrl("jdbc:mysql://localhost:3306/myschema" );
        jdbcRs.setUsername("root");
        jdbcRs.setPassword("1420");
        jdbcRs.setCommand("select * from emp");
        jdbcRs.execute();
        System.out.println("-----------Using RowSet------------\n");
        while (jdbcRs.next()){
            System.out.print("ID "+jdbcRs.getString(1));
            System.out.println(" Name "+jdbcRs.getString(2)+" sal "+jdbcRs.getString(3));
        }
        jdbcRs.close();

        var ds=MyDataSourceFactory.getMYSQLDataSource();
        Connection conn =ds.getConnection();
        var jdbcCrs = myRSF.createCachedRowSet();
//        jdbcCrs.setUrl("jdbc:mysql://localhost:3306/myschema" );
//        jdbcCrs.setUsername("root");
//        jdbcCrs.setPassword("1420");
        jdbcCrs.setCommand("select * from emp");
        conn.setAutoCommit(false);
        jdbcCrs.execute(conn);
        System.out.println("\n\n-----------Using CachedRowSet------------");
        System.out.println("-----------Before Applying the updates------------\n");
        while (jdbcCrs.next()){
            System.out.print("ID "+jdbcCrs.getString(1));
            System.out.println(" Name "+jdbcCrs.getString(2)+" sal "+jdbcCrs.getString(3));
        }

        jdbcCrs.moveToInsertRow();
        jdbcCrs.updateInt(1,8);
        jdbcCrs.updateString(2,"h");
        jdbcCrs.updateInt(3,50);
        jdbcCrs.updateString(4,"h");
        jdbcCrs.insertRow();
        jdbcCrs.moveToCurrentRow();

        jdbcCrs.absolute(2);
        jdbcCrs.deleteRow();

        jdbcCrs.acceptChanges();

        jdbcCrs.close();
    }
}