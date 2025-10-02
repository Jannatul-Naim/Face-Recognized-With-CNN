package back.face.core;

import ai.onnxruntime.*;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.Arrays;

public class FaceVerification {

    private static OrtEnvironment env;
    private static OrtSession session;
    private static boolean initialized = false;

    // Initialize ONNX model once
    public static void init() throws Exception {
        if (initialized) return;

        env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();

        String modelPath = ModelLoader.loadModelFromResource("/back.face.model/faceNet.onnx");
        session = env.createSession(modelPath, opts);

        System.out.println("✅ Model loaded");
        for (NodeInfo info : session.getInputInfo().values()) {
            if (info.getInfo() instanceof TensorInfo tensorInfo) {
                System.out.println(info.getName() + " : " + Arrays.toString(tensorInfo.getShape()));
            }
        }
        initialized = true;
    }

    // Verify using two file paths
    public static boolean verify(String img1Path, String img2Path) throws Exception {
        init();
        float[] img1Data = ImagePreprocessor.preprocess(img1Path);
        float[] img2Data = ImagePreprocessor.preprocess(img2Path);

        float[] embedding1 = EmbeddingExtractor.getEmbedding(session, env, img1Data);
        float[] embedding2 = EmbeddingExtractor.getEmbedding(session, env, img2Data);

        double similarity = SimilarityCalculator.cosineSimilarity(embedding1, embedding2);
        System.out.println("Cosine similarity: " + similarity);

        return similarity > 0.8;
    }


    // Verify using a live frame vs reference image
    public static boolean verify(Mat m) throws Exception {
        init();
        float[] img1Data = ImagePreprocessor.preprocess("qq.png");  // reference
        float[] img2Data = ImagePreprocessor.preprocess(m);

        float[] embedding1 = EmbeddingExtractor.getEmbedding(session, env, img1Data);
        float[] embedding2 = EmbeddingExtractor.getEmbedding(session, env, img2Data);

        double similarity = SimilarityCalculator.cosineSimilarity(embedding1, embedding2);
        System.out.println("Cosine similarity: " + similarity);

        return similarity > 0.7;
    }

    // Clean up
    public static void close() throws OrtException {
        if (session != null) session.close();
        if (env != null) env.close();
        initialized = false;
    }
}
