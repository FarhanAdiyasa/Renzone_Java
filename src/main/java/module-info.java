module com.example.renzone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires fontawesomefx;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires io.github.gleidsonmt.gncontrols;
    requires itextpdf;
    requires java.desktop;
    requires jasperreports;

    opens com.example.renzone to javafx.fxml;
    opens com.example.renzone.models to javafx.base;
    opens com.example.renzone.controller to javafx.fxml;

    exports com.example.renzone;
    exports com.example.renzone.controller;

}
