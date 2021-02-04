module com.arabie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires java.sql;
    requires mysql.connector.java;

    opens com.arabie to javafx.fxml;

    exports com.arabie;
}