package back.face.ui;

import back.face.core.FaceVerification;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.awt.image.BufferedImage;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2RGB;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

public class CameraCapture {

    private final VideoCapture capture;
    private final Mat frame;

    public CameraCapture() {
        capture = new VideoCapture(0); // Default camera
        frame = new Mat();
    }

    public WritableImage grabFrame() {
        if (capture.isOpened()) {
            capture.read(frame);
            return mat2Image(frame);
        }
        return null;
    }

    public void saveImage() {
        if (!frame.empty()) {
            String filename = "photo_" + System.currentTimeMillis() + ".png";
            imwrite(filename, frame);
            System.out.println("✅ Saved: " + filename);
        }
    }

    private WritableImage mat2Image(Mat mat) {
        try {
            Mat converted = new Mat();
            cvtColor(mat, converted, COLOR_BGR2RGB);

            BufferedImage bImage = new BufferedImage(
                    converted.cols(), converted.rows(),
                    BufferedImage.TYPE_3BYTE_BGR
            );

            byte[] data = new byte[(int) (converted.total() * converted.channels())];
            converted.data().get(data);
            bImage.getRaster().setDataElements(0, 0, converted.cols(), converted.rows(), data);

            return SwingFXUtils.toFXImage(bImage, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void release() {
        if (capture.isOpened()) {
            capture.release();
        }
    }

    public void checkImage() {
        try {
            if (FaceVerification.verify(frame)) {
                System.out.println("✅ Face match");
            } else {
                System.out.println("❌ Not a match");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
