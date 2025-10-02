package back.face.core;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;

public class ImagePreprocessor {

    // Preprocess from file path
    public static float[] preprocess(String path) {
        Mat img = org.bytedeco.opencv.global.opencv_imgcodecs.imread(path);
        if (img.empty()) throw new RuntimeException("Image not found: " + path);
        return preprocess(img);
    }

    // Preprocess from Mat (camera frame, etc.)
    public static float[] preprocess(Mat img) {
        if (img == null || img.empty()) {
            throw new RuntimeException("Invalid image (null or empty)");
        }

        // Convert BGR → RGB
        opencv_imgproc.cvtColor(img, img, opencv_imgproc.COLOR_BGR2RGB);
        // Resize to 160x160
        opencv_imgproc.resize(img, img, new Size(160, 160));
        // Convert to float and scale
        img.convertTo(img, opencv_core.CV_32F, 1.0 / 255.0, 0.0);

        // Normalize [-1, 1]
        Mat mean = new Mat(img.size(), img.type(), new Scalar(0.5, 0.5, 0.5, 0));
        Mat std = new Mat(img.size(), img.type(), new Scalar(0.5, 0.5, 0.5, 0));
        opencv_core.subtract(img, mean, img);
        opencv_core.divide(img, std, img);

        // Extract raw float data
        float[] data = new float[(int) (img.total() * img.channels())];
        new FloatPointer(img.data()).get(data);

        return data;
    }
}
