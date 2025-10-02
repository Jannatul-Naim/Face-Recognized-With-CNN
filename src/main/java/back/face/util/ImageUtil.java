package back.face.util;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.javacpp.indexer.FloatIndexer;

public class ImageUtil {

    /**
     * Load and preprocess an image for FaceNet/ONNX/TensorFlow.
     * Output: float array in NHWC format (1, 160, 160, 3) with values in [-1,1].
     *
     * @param path Path to the image
     * @return float[] suitable as FaceNet input
     */
    public static float[] loadAndPreprocess(String path) {
        // Load image (OpenCV loads as BGR by default)
        Mat img = opencv_imgcodecs.imread(path);
        if (img.empty()) {
            throw new RuntimeException("Image not found: " + path);
        }

        try {
            // Convert BGR -> RGB
            opencv_imgproc.cvtColor(img, img, opencv_imgproc.COLOR_BGR2RGB);

            // Resize to 160x160
            Mat resized = new Mat();
            opencv_imgproc.resize(img, resized, new Size(160, 160));

            // Convert to float32 [0,1]
            Mat floatImg = new Mat();
            resized.convertTo(floatImg, opencv_core.CV_32F, 1.0 / 255.0, 0.0);

            // Normalize to [-1,1]
            Mat normalized = new Mat();
            floatImg.convertTo(normalized, opencv_core.CV_32F, 2.0, -1.0);

            // Extract pixel data into float[]
            int height = 160, width = 160, channels = 3;
            float[] data = new float[height * width * channels]; // NHWC with batch=1
            FloatIndexer idx = normalized.createIndexer();

            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int c = 0; c < channels; c++) {
                        data[index++] = idx.get(y, x, c);
                    }
                }
            }
            idx.release();

            // Cleanup
            resized.release();
            floatImg.release();
            normalized.release();

            return data;
        } finally {
            img.release();
        }
    }
}
