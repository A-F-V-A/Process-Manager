module edu.est.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jakarta.mail;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens edu.est.process.manager.infrastructure to javafx.fmxl;
    exports edu.est.process.manager.infrastructure;

    opens edu.est.process.manager.infrastructure.javafx.controllers to javafx.fmxl;
    exports edu.est.process.manager.infrastructure.javafx.controllers;

    opens edu.est.process.manager.infrastructure.javafx.components to javafx.fmxl;
    exports edu.est.process.manager.infrastructure.javafx.components;
/*
    opens  edu.est.library.domain.models to javafx.fxml;
    exports edu.est.library.domain.models;

    opens  edu.est.library.domain.dto to javafx.fxml;
    exports edu.est.library.domain.dto;*/
}