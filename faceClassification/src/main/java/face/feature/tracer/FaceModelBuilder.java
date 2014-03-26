package face.feature.tracer;

import face.feature.classifier.FaceClassification;
import face.feature.extraction.CropFace;
import face.feature.extraction.FaceTrainDataGenerator;
import face.feature.extraction.ImageScaler;

public class FaceModelBuilder {
	public static void run() {
		CropFace cf = new CropFace();
		cf.batchCorpFaces();
		ImageScaler.batchResizeImage();
		FaceTrainDataGenerator.run();
		FaceClassification.faceClassify();
	}
	
	public static void main(String[] args) {
		run();
	}
}
