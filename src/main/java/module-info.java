module edu.est.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.google.gson;

    opens edu.est.process.manager.infrastructure to javafx.fmxl;
    exports edu.est.process.manager.infrastructure;

    opens edu.est.process.manager.infrastructure.javafx.controllers to javafx.fmxl;
    exports edu.est.process.manager.infrastructure.javafx.controllers;

    opens edu.est.process.manager.infrastructure.javafx.components to javafx.fmxl;
    exports edu.est.process.manager.infrastructure.javafx.components;

    opens  edu.est.process.manager.domain.models to javafx.fmxl;
    exports edu.est.process.manager.domain.models;

    opens  edu.est.process.manager.domain.util to javafx.fmxl;
    exports edu.est.process.manager.domain.util;

    opens  edu.est.process.manager.domain.structures to javafx.fmxl;
    exports edu.est.process.manager.domain.structures;

}