package back.face.core;

import java.io.*;
import java.nio.file.*;

public class ModelLoader {
    public static String loadModelFromResource(String resourcePath) throws IOException {
        try (InputStream in = FaceVerification.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new FileNotFoundException("Model not found: " + resourcePath);
            Path tempFile = Files.createTempFile("onnx_model", ".onnx");
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile.toAbsolutePath().toString();
        }
    }
}
