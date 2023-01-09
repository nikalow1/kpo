module com.example.kpo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires spring.security.crypto;


    opens com.example.kpo to javafx.fxml;
    exports com.example.kpo;
    exports com.example.kpo.DataType;
    opens com.example.kpo.DataType to javafx.fxml;

    exports com.example.kpo.Function;
    opens com.example.kpo.Function to javafx.fxml;
}