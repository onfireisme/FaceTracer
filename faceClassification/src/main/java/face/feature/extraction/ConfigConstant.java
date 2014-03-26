package face.feature.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConfigConstant {
//	private static File directory = new File("");
	public static final String path = "E:/WorkSpaces/faceData/";
	public static final String cropImagePath = path + "cropPic/";
	public static final String scaleImagePath = path + "scalePic/";
	public static final String rawScaleImagePath = path + "scalePic/raw/";
	public static final String testImagePath = path + "testPic/";
	public static final String misMatchPicPath = path + "misMatchPic/";
	public static final String modelPath = path + "model/";
	public static final String trainPath = path + "trainData/";
	public static final String rawPath = path + "rawData/";
	
	public static final String Train = "Train";
	public static final String Raw = "Raw";
	
	public static final String intensityAttributes = "Intensity.arff"; 
	public static final String intensityHistogramAttributes = "IntensityHistogram.arff"; 
	public static final String intensityStatisticsAttributes = "IntensityStatistics.arff"; 
	public static final String intensityMeanAttributes = "IntensityMean.arff"; 
	public static final String intensityEnergyAttributes = "IntensityEnergy.arff"; 
	public static final String RGBAttributes = "RGB.arff"; 
	public static final String RGBMeanAttributes = "RGBMean.arff"; 
	public static final String RGBEnergyAttributes = "RGBEnergy.arff"; 
	public static final String RGBStatisticsAttributes = "RGBStatistics.arff"; 
	public static final String HSVAttributes = "HSV.arff"; 
	public static final String HSVStatisticsAttributes = "HSVStatistics.arff";
	public static final String edgeMagnitudeAttributes = "EdgeMagnitude.arff";
	public static final String edgeMagnitudeMeanAttributes = "EdgeMagnitudeMean.arff";
	public static final String edgeMagnitudeEnergyAttributes = "EdgeMagnitudeEnergy.arff";
	public static final String edgeOrientationAttributes = "EdgeOrientation.arff";
	
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
	public static final String gender = "gender";
	public static final String mustache = "mustache";
	public static final String dataModel = "dataModel";
	
	public static final String gender_attritube = "gender {male, female}";
	public static final String mustache_attritube = "mustache {true, false}";
	
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
	}
	
	public static final HashMap<String, String> attributeMap = new HashMap<String, String>();
	static {
		attributeMap.put(gender, gender_attritube);
		attributeMap.put(mustache, mustache_attritube);
	}
	
	public static final HashMap<String, String> attributeValueMap = new HashMap<String, String>();
	static {
		attributeValueMap.put(gender + A, "male");
		attributeValueMap.put(gender + B, "female");
		attributeValueMap.put(mustache + A, "true");
		attributeValueMap.put(mustache + B, "false");
	}
	
	public static final List<String> categoryList = new ArrayList<String>();
	static {
		categoryList.add(gender);
		categoryList.add(mustache);
	}
}
