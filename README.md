For test entrance:
FaceTracer/faceClassification/src/test/java/face/tracer/test/PreprocessClassificationTest.java build face data model, main steps including:crop face picture to get key regions,
scaling, generating training data(.arff),training data for face classifition svm+adaboost.

FaceTracer/faceClassification/src/test/java/face/tracer/test/PredictorTest Predict human face picture.

For main classes explaination:
face.feature.classifier.FaceClassification   Read training data file '.arff' from faceData/trainData/ folder, then output result in faceData/model/.
face.feature.classifier.FaceClassifier       A value object for restoring the attributes which are used for prediction.
face.feature.classifierPredictFaceClassifier Read data model file from faceData/model/ then process face classification prediction.
face.feature.extraction.AffineTransform      Call matlab affine transformation to get the key points(like whole face, eye, mounth, nose etc.).
face.feature.extraction.ConfigConstant       Restoring all the configuration constant.
face.feature.extraction.CropFace             After getting all the key points, crop the picture to get some useful face region.
face.feature.extraction.FaceDataProcess      A class for collecting all the original images for image db.
face.feature.extraction.FaceFeaturePointsExtraction Read keypoints from ‘FaceTracer/faceData/LandMarks1.txt’
face.feature.extraction.FaceIndex            A value object for restoring the basic information for each face image.
face.feature.extraction.FaceRawDataGenerator To generate raw data for face prediction.
face.feature.extraction.FaceTrainDataGenerator To generate training data file (.arff) form the scalled picture. All the attributes for face classification will be created in this class.
face.feature.extraction.ImageScaler	     To scale the picture in a equal size. Each face region has its size defined in ConfigConstant.
