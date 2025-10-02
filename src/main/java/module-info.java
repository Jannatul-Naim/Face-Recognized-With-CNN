module back.face {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.bytedeco.opencv;
    requires com.microsoft.onnxruntime;

    opens back.face.controller to javafx.fxml;
    exports back.face.ui;
    exports back.face.controller;
}
