package com.arabie;


import javax.sql.rowset.JoinRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class App {

    public static void main(String[] args) throws SQLException {

        RowSetFactory myRSF = RowSetProvider.newFactory();

        var ds=MyDataSourceFactory.getMYSQLDataSource();
        Connection conn =ds.getConnection();
        conn.setAutoCommit(false);
        JoinRowSet joinRowSet = myRSF.createJoinRowSet();

        var jdbcCrs1 = myRSF.createCachedRowSet();
        jdbcCrs1.setCommand("select * from country");
        jdbcCrs1.execute(conn);

        var jdbcCrs2 = myRSF.createCachedRowSet();
        jdbcCrs2.setCommand("select * from city");
        jdbcCrs2.execute(conn);
        joinRowSet.addRowSet(jdbcCrs1,"country_id");
        joinRowSet.addRowSet(jdbcCrs2,"country_id");

        while (joinRowSet.next()){
            System.out.println(joinRowSet.getString("city")+"\t"
                    +joinRowSet.getString("country"));
        }

        jdbcCrs1.close();
        jdbcCrs2.close();
        joinRowSet.close();

    }
}