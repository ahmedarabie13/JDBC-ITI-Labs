module com.arabie {

    opens com.arabie to javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    exports com.arabie;
}