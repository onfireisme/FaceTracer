package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ConfigConstant {
//	private static File directory = new File("");
	public static final String DB_HOST = "";//to be setup as needed
	public static final String DB_NAME = "faceTracer";
	public static final String TABLE_NAME = "n5faceLabels";//"faceLabels";//"nfaceLabels";//"n2faceLabels"//"faceLabels";//"n3faceLabels";
	public static final String path = "D:/Tomcat 6.0/webapps/searchEngine/";
	public static final String cropImagePath = path + "temp/cropPic/";
	public static final String landMarkPath = path + "landMark/landMark.txt";
	public static final String rawScaleImagePath = path + "temp/scalePic/";
	public static final String tempImagePath = path + "temp/";
	public static final String modelPath = path + "model/";
	public static final String rawPath = path + "temp/rawData/";
	public static final String testPic = "test.bmp";
	
	public static final String Train = "Train";
	public static final String Raw = "Raw";
	public static final String PicSuffix = ".bmp";
	public static final String PicFormat = "bmp";
	
	
	public static final String intensityAttributes = "Intensity.arff"; 
	public static final String intensityHistogramAttributes = "IntensityHistogram.arff"; 
	public static final String intensityStatisticsAttributes = "IntensityStatistics.arff"; 
	public static final String intensityMeanAttributes = "IntensityMean.arff"; 
	public static final String intensityEnergyAttributes = "IntensityEnergy.arff"; 
	public static final String RGBAttributes = "RGB.arff"; 
	public static final String RGBHistogramAttributes = "RGBHistogram.arff"; 
	public static final String RGBMeanAttributes = "RGBMean.arff"; 
	public static final String RGBEnergyAttributes = "RGBEnergy.arff"; 
	public static final String RGBStatisticsAttributes = "RGBStatistics.arff"; 
	public static final String HSVAttributes = "HSV.arff"; 
	public static final String HSVStatisticsAttributes = "HSVStatistics.arff";
	public static final String edgeMagnitudeAttributes = "EdgeMagnitude.arff";
	public static final String edgeMagnitudeMeanAttributes = "EdgeMagnitudeMean.arff";
	public static final String edgeMagnitudeEnergyAttributes = "EdgeMagnitudeEnergy.arff";
	public static final String edgeOrientationAttributes = "EdgeOrientation.arff";
	
	public static final String picEvaluationFile = "picEvaluation.arff";
	
	public static final String faceLabelName = "facelabels.txt";
	public static final String faceIndexName = "faceindex.txt";
	public static final String faceStatsName = "facestats.txt";
	public static final String faceDataName = "facedata.txt";
	public static final String faceLandMarks = "LandMarks1.txt";
	public static final String wholeFace = "wholeFace/";
	public static final String eye = "eye/";
	public static final String mouth = "mouth/";
	public static final String hair = "hair/";
	public static final String nose = "nose/";
	public static final String cheeks = "cheeks/";
	public static final String chin = "chin/";
	public static final String forehead = "forehead/";
	public static final String eyebrows = "eyebrows/";
	public static final String upperLip = "upperLip/";
	public static final String background = "background/";
	
	public static final String gender = "gender";
	public static final String mustache = "mustache";
	public static final String hair_color = "hair_color";
	public static final String smiling = "smiling";
	public static final String blurry = "blurry";
	public static final String lighting = "lighting";
	public static final String environment = "environment";
	public static final String age = "age";
	public static final String race = "race";
	public static final String eye_wear = "eye_wear";
	public static final String asian = "asian";
	public static final String white = "white";
	public static final String black = "black";
	public static final String baby = "baby";
	public static final String child = "child";
	public static final String youth = "youth";
	public static final String middle_aged = "middle_aged";
	public static final String senior = "senior";
	public static final String none = "none";
	public static final String eyeglasses = "eyeglasses";
	public static final String sunglasses = "sunglasses";
	
	public static final String dataModel = "dataModel";
	
	public static final String testResult = "TestResult.txt";
	
	public static final String gender_attritube = "gender {male, female}";
	public static final String mustache_attritube = "mustache {true, false}";
	public static final String hair_color_attritube = "hair_color {blond, not_blond}";
	public static final String smiling_attritube = "smiling {true, false}";
	public static final String blurry_attritube = "blurry {true, false}";
	public static final String lighting_attritube = "lighting {harsh, flash}";
	public static final String environment_attritube = "environment {outdoor, indoor}";
	public static final String asian_attritube = "asian {true, false}";
	public static final String white_attritube = "white {true, false}";
	public static final String black_attritube = "black {true, false}";
	public static final String baby_attritube = "baby {true, false}";
	public static final String child_attritube = "child {true, false}";
	public static final String youth_attritube = "youth {true, false}";
	public static final String middle_aged_attritube = "middle_aged {true, false}";
	public static final String senior_attritube = "senior {true, false}";
	public static final String none_attritube = "none {true, false}";
	public static final String eyeglasses_attritube = "eyeglasses {true, false}";
	public static final String sunglasses_attritube = "sunglasses {true, false}";
	
	public static final List<String> classValueList = new LinkedList<String>();
	static {
		classValueList.add(smiling+":true");
		classValueList.add(smiling+":false");
		classValueList.add(gender+":male");
		classValueList.add(gender+":female");
		classValueList.add(mustache+":true");
		classValueList.add(mustache+":false");
		classValueList.add(hair_color+":blond");
		classValueList.add(hair_color+":not_blond");
		classValueList.add(blurry+":true");
		classValueList.add(blurry+":false");
		classValueList.add(lighting+":harsh");
		classValueList.add(lighting+":flash");
		classValueList.add(environment+":outdoor");
		classValueList.add(environment+":indoor");
		classValueList.add(race+":asian");
		classValueList.add(race+":white");
		classValueList.add(race+":black");
		classValueList.add(age+":baby");
		classValueList.add(age+":child");
		classValueList.add(age+":youth");
		classValueList.add(age+":middle_aged");
		classValueList.add(age+":senior");
		classValueList.add(eye_wear+":none");
		classValueList.add(eye_wear+":eyeglasses");
		classValueList.add(eye_wear+":sunglasses");
	}
	
	public static final String imgSurffix = "bmp";
	
	public static final String A = "A";
	public static final String B = "B";
	
	public static final int testFold = 5;
	public static final int iteration = 10;
	
	public static final List<String> faceRegions = new ArrayList<String>();
	static {
		faceRegions.add(wholeFace);
		faceRegions.add(eye);
		faceRegions.add(mouth);
		faceRegions.add(hair);
		faceRegions.add(nose);
		faceRegions.add(cheeks);
		faceRegions.add(chin);
		faceRegions.add(forehead);
		faceRegions.add(eyebrows);
		faceRegions.add(upperLip);
		faceRegions.add(background);
	}
	
	public static final HashMap<String, int[]> faceRegionsSize = new HashMap<String, int[]>();
	static {
		int[] s1={26, 30};
		faceRegionsSize.put(wholeFace, s1);
		int[] s2={30, 10};
		faceRegionsSize.put(eye, s2);
		int[] s3={20, 12};
		faceRegionsSize.put(mouth, s3);
		int[] s4={30, 10};
		faceRegionsSize.put(hair, s4);
		int[] s5={10, 20};
		faceRegionsSize.put(nose, s5);
		int[] s6={7, 20};
		faceRegionsSize.put(cheeks, s6);
		int[] s7={14, 6};
		faceRegionsSize.put(chin, s7);
		int[] s8={25, 6};
		faceRegionsSize.put(forehead, s8);
		int[] s9={35, 5};
		faceRegionsSize.put(eyebrows, s9);
		int[] s10={13, 5};
		faceRegionsSize.put(upperLip, s10);
		int[] s11={40, 30};
		faceRegionsSize.put(background, s11);
	}
	
	public static final HashMap<String, String> attributeMap = new HashMap<String, String>();
	static {
		//TODO be uncommented
		attributeMap.put(smiling, smiling_attritube);
		attributeMap.put(gender, gender_attritube);
		attributeMap.put(mustache, mustache_attritube);
		attributeMap.put(hair_color, hair_color_attritube);
		attributeMap.put(blurry, blurry_attritube);
		attributeMap.put(lighting, lighting_attritube);
		attributeMap.put(environment, environment_attritube);
		attributeMap.put(asian, asian_attritube);
		attributeMap.put(black, black_attritube);
		attributeMap.put(white, white_attritube);
		attributeMap.put(baby, baby_attritube);
		attributeMap.put(child, child_attritube);
		attributeMap.put(youth, youth_attritube);
		attributeMap.put(middle_aged, middle_aged_attritube);
		attributeMap.put(senior, senior_attritube);
		attributeMap.put(none, none_attritube);
		attributeMap.put(eyeglasses, eyeglasses_attritube);
		attributeMap.put(sunglasses, sunglasses_attritube);
	}
	
	public static final HashMap<String, String> attributeValueMap = new HashMap<String, String>();
	static {
		attributeValueMap.put(gender + A, "male");
		attributeValueMap.put(gender + B, "female");
		attributeValueMap.put(mustache + A, "true");
		attributeValueMap.put(mustache + B, "false");
		attributeValueMap.put(hair_color + A, "blond");
		attributeValueMap.put(hair_color + B, "not_blond");
		attributeValueMap.put(smiling + A, "true");
		attributeValueMap.put(smiling + B, "false");
		attributeValueMap.put(blurry + A, "true");
		attributeValueMap.put(blurry + B, "false");
		attributeValueMap.put(lighting + A, "harsh");
		attributeValueMap.put(lighting + B, "flash");
		attributeValueMap.put(environment + A, "outdoor");
		attributeValueMap.put(environment + B, "indoor");
	}
	
	public static final List<String> multiClassLabels = new ArrayList<String>();
	static {
		multiClassLabels.add(race);
		multiClassLabels.add(age);
		multiClassLabels.add(eye_wear);
	}
	
	public static final HashMap<String, List<String>> multiClassAttributeValueMap = new HashMap<String, List<String>>();
	public static final List<String> raceClasses = new ArrayList<String>();
	public static final List<String> ageClasses = new ArrayList<String>();
	public static final List<String> eye_wearClasses = new ArrayList<String>();
	public static final List<String> multiClassAttrs = new ArrayList<String>();
	static {
		raceClasses.add(asian);
		raceClasses.add(black);
		raceClasses.add(white);
		multiClassAttributeValueMap.put(race, raceClasses);
		
		ageClasses.add(baby);
		ageClasses.add(child);
		ageClasses.add(youth);
		ageClasses.add(middle_aged);
		ageClasses.add(senior);
		multiClassAttributeValueMap.put(age, ageClasses);
		
		eye_wearClasses.add(none);
		eye_wearClasses.add(eyeglasses);
		eye_wearClasses.add(sunglasses);
		multiClassAttributeValueMap.put(eye_wear, eye_wearClasses);
		
		multiClassAttrs.addAll(raceClasses);
		multiClassAttrs.addAll(ageClasses);
		multiClassAttrs.addAll(eye_wearClasses);
	}
	
}
