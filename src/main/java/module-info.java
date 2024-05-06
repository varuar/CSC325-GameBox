module org.openjfx.gamebox {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires google.cloud.core;
    requires com.google.api.apicommon;


    opens org.openjfx.gamebox to javafx.fxml;
    exports org.openjfx.gamebox;

}



