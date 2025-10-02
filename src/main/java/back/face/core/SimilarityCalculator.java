package back.face.core;

//public class SimilarityCalculator {
//    public static double cosineSimilarity(float[] a, float[] b) {
//        double dot = 0, normA = 0, normB = 0;
//        for (int i = 0; i < a.length; i++) {
//            dot += a[i] * b[i];
//            normA += a[i] * a[i];
//            normB += b[i] * b[i];
//        }
//        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
//    }
//}
public class SimilarityCalculator {

    public static double cosineSimilarity(float[] a, float[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input vectors must not be null");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException("Input vectors must have the same length");
        }

        double dot = 0.0, normA = 0.0, normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        return (denominator == 0) ? 0.0 : dot / denominator;
    }
}
