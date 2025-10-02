package back.face.controller;

import back.face.ui.CameraCapture;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class CameraController {

    public Button checkBtn;
    @FXML
    private ImageView cameraView;
    @FXML
    private Button captureBtn;

    private CameraCapture cameraCapture;
    private AnimationTimer timer;

    @FXML
    public void initialize() {
        cameraCapture = new CameraCapture();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (cameraCapture != null) {
                    cameraView.setImage(cameraCapture.grabFrame());
                }
            }
        };
        timer.start();

        captureBtn.setOnAction(e -> cameraCapture.saveImage());
        checkBtn.setOnAction(e -> {
            try {
                cameraCapture.checkImage();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void stop() {
        if (timer != null) timer.stop();
        if (cameraCapture != null) cameraCapture.release();
    }
}
