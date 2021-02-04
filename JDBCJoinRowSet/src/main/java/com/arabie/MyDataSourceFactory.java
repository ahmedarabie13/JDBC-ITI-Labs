package com.arabie;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Properties;


public class MyDataSourceFactory {
    private static MysqlDataSource mysqlDS = null;

    public  static DataSource getMYSQLDataSource(){
        Properties prop = new Properties();
        FileInputStream fis ;
        try {

            fis = new FileInputStream("db.properties");
            prop.load(fis);
            if(mysqlDS==null) {
                synchronized (MysqlDataSource.class){
                    if(mysqlDS==null) {
                        mysqlDS = new MysqlDataSource();
                        mysqlDS.setURL(prop.getProperty("MYSQL_DB_URL"));
                        mysqlDS.setUser(prop.getProperty("MYSQL_DB_USERNAME"));
                        mysqlDS.setPassword(prop.getProperty("MYSQL_DB_PASSWORD"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}
