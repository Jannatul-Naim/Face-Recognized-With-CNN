package back.face.core;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_dnn.Net;

public class FaceDetection {

    public static void main(String[] args) {
        // Paths to model files
        String proto = "deploy.prototxt"; // define path to your prototxt
        String model = "res10_300x300_ssd_iter_140000.caffemodel"; // define path to caffemodel

        // Load pre-trained model
        Net net = opencv_dnn.readNetFromCaffe(proto, model);

        // Load image
        Mat image = opencv_imgcodecs.imread("input.jpg");
        if (image.empty()) {
            System.out.println("Could not read input image!");
            return;
        }

        // Create blob from image
        Mat blob = opencv_dnn.blobFromImage(
                image,
                1.0,                                 // scale factor
                new Size(300, 300),                  // resize
                new Scalar(104.0, 177.0, 123.0, 0.0),// mean subtraction
                false,                               // swapRB
                false,                               // crop
                opencv_core.CV_32F                   // depth
        );

        // Set input and forward pass
        net.setInput(blob);
        Mat detections = net.forward();

        int cols = image.cols();
        int rows = image.rows();

        // Loop through detections
        for (int i = 0; i < detections.size(2); i++) {
            FloatPointer data = new FloatPointer(detections.ptr(0, 0, i));

            float confidence = data.get(2); // confidence score
            if (confidence > 0.5) { // threshold
                int x1 = Math.round(data.get(3) * cols);
                int y1 = Math.round(data.get(4) * rows);
                int x2 = Math.round(data.get(5) * cols);
                int y2 = Math.round(data.get(6) * rows);

                // Draw rectangle around face
                opencv_imgproc.rectangle(image,
                        new Point(x1, y1),
                        new Point(x2, y2),
                        new Scalar(0, 255, 0, 0),
                        2, 8, 0);

                // Print detection info
                System.out.printf("Face detected: [%.2f] at (%d, %d, %d, %d)%n", confidence, x1, y1, x2, y2);
            }
        }

        // Save output
        opencv_imgcodecs.imwrite("output.jpg", image);
        System.out.println("Face detection finished. Result saved to output.jpg");
    }
}
