package back.face.ui;

import back.face.controller.CameraController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CameraApp extends Application {

    private CameraController controller;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/face/ui/camera_view.fxml"));
            Scene scene = new Scene(loader.load());
            controller = loader.getController();

            stage.setTitle("Face Camera App");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(e -> {
                if (controller != null) controller.stop();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch();
    }
}
