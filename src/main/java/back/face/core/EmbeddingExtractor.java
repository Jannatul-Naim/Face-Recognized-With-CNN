package back.face.core;

import ai.onnxruntime.*;
import java.nio.FloatBuffer;
import java.util.Collections;

public class EmbeddingExtractor {

    public static float[] getEmbedding(OrtSession session, OrtEnvironment env, float[] imgData) throws OrtException {
        String inputName = session.getInputNames().iterator().next();
        try (OnnxTensor tensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(imgData), new long[]{1, 160, 160, 3});
             OrtSession.Result result = session.run(Collections.singletonMap(inputName, tensor))) {
            float[][] embedding = (float[][]) result.get(0).getValue();
            return embedding[0];
        }
    }
}
