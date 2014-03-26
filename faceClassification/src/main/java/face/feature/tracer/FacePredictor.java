package face.feature.tracer;

import face.feature.classifier.PredictFaceClassifier;
import face.feature.extraction.FaceRawDataGenerator;

public class FacePredictor {
	public static void run() {
		FaceRawDataGenerator fg = new FaceRawDataGenerator();
		fg.generateRawData();
		PredictFaceClassifier.run();
	}
	
	public static void main(String[] args) {
		run();
	}
}
