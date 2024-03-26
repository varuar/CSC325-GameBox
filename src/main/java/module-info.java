module org.openjfx.gamebox {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.openjfx.gamebox to javafx.fxml;
    exports org.openjfx.gamebox;
}