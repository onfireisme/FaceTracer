package face.feature.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import face.feature.extraction.ConfigConstant;
import face.feature.extraction.CropFace;

import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class FaceClassification {
	
	public static void main(String[] args) {
		faceClassify();
	}
	
	public static void faceClassify() {
		Map<String, List<String>> sortedfileNames = getTrainfileNames();
		
		for (String name : sortedfileNames.keySet()) {
			combineSVM_adaBoost(name, sortedfileNames.get(name));
		}
	}
	
	private static Map<String, List<String>> getTrainfileNames() {
		File file=new File(ConfigConstant.trainPath);
		String subPaths[];
		subPaths=file.list();
		
		Map<String, List<String>> sortedfileNames = new HashMap<String, List<String>>();
		for (String path : subPaths) {
			if (path.contains("raw")) {
				continue;
			}
			file=new File(ConfigConstant.trainPath+path+"/");
			String[] subSubPaths = file.list();
			for (String subSubpath : subSubPaths) {
				file=new File(ConfigConstant.trainPath+path+"/"+subSubpath+"/");
				String[] fileNames = file.list();
				
				List<String> fileNameList = new ArrayList<String>();
				for(String name:fileNames) {
					fileNameList.add(ConfigConstant.trainPath+path+"/"+subSubpath+"/"+name);
				}
				if (sortedfileNames.get(subSubpath)!=null) {
					List<String> nameList = sortedfileNames.get(subSubpath);
					nameList.addAll(fileNameList);
					sortedfileNames.put(subSubpath , nameList);
				} else {
					sortedfileNames.put(subSubpath, fileNameList);
				}
			}
		}

		return sortedfileNames;
	}
	
	private static void combineSVM_adaBoost(String name, List<String> fileNames) {
		List<Instances> instancesList = new ArrayList<Instances>();
		
		List<double[]> weightsList = new ArrayList<double[]>();
		List<Double> biasList = new ArrayList<Double>();
		List<Double> Bts = new ArrayList<Double>();
		List<Integer> classifierIds = new ArrayList<Integer>();
		
		List<List<Integer>> errorInstancePos = getSVMErroroutput(fileNames, instancesList, weightsList, biasList);
		
		adaBoost(errorInstancePos, instancesList, Bts, classifierIds);
		FaceClassifier fc = new FaceClassifier(weightsList, biasList, Bts, classifierIds);
		persistentObject(name, fc);
	}
	
	private static void persistentObject(String name, FaceClassifier fc) {
		try {
			CropFace.createDirectory(ConfigConstant.modelPath+ name +"/");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ConfigConstant.modelPath+ name +"/"+ConfigConstant.dataModel));
			oos.writeObject(fc);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void adaBoost(List<List<Integer>> errorInstancePos, List<Instances> instancesList, List<Double> Bts, List<Integer> classifierIds) {
		double[] w = new double[instancesList.get(0).size()];
		
		initInstanceWeightValues(w, instancesList.get(0));
		
		for (int i=0; i < ConfigConstant.iteration; i++) {
			
			normalizeInstanceWeights(w);
			List<Double> errorList = new ArrayList<Double>();
			
			for (int j=0; j < errorInstancePos.size(); j++) {
				List<Integer> errorPos = errorInstancePos.get(j);
			
				//compute error rate
				double error = 0;
				for (Integer position: errorPos) {
					error += w[position];
				}
				errorList.add(error);
			}
			int classifierId = getMinErrorIndex(errorList);
//			if (classifierIds.contains(classifierId)) {
//				break;
//			}
			
			double error = errorList.get(classifierId);
			if (error >= 0.5) {
				break;
			}
			double Bt = error/(1-error);
			Bts.add(Bt);
			classifierIds.add(classifierId);
			updateInstanceWeights(w, errorInstancePos.get(classifierId), instancesList.get(classifierId), Bt);
			System.out.println("error rate: " +error);
		}
		System.out.println("bts: " + Bts);
		System.out.println("classifierIds: " + classifierIds);
	}
	
	private static void updateInstanceWeights(double[] weights, List<Integer> errorPos, Instances instances, double Bt) {
		for (int i = 0; i < instances.size(); i++) {
			if (!errorPos.contains(i)) {
				weights[i] = weights[i]*Bt;
			}
		}
	}
	
	private static int getMinErrorIndex(List<Double> errorList) {
		double min = Double.MAX_VALUE;
		int id = 0;
		for (int i=0; i < errorList.size(); i++) {
			if (errorList.get(i)<min) {
				id = i;
				min = errorList.get(i);
			}
		}
		return id;
	}
	
	private static void normalizeInstanceWeights(double[] weights) {
		double sum = 0;
		for (int i=0; i < weights.length; i++) {
			sum += weights[i];
		}
		for (int i=0; i < weights.length; i++) {
			weights[i] /= sum;
		}
	}
	
	private static void initInstanceWeightValues(double[] weights, Instances instances) {
		int trueCount=0;
		for (int i=0; i < instances.size(); i++) {
			Instance ins = instances.instance(i);
			if (ins.classValue()==0) {
				trueCount++;
			}
		}
		for (int i=0; i < weights.length; i++) {
			if (instances.get(i).classValue()==0) {
				weights[i] =  0.5/trueCount;
			} else {
				weights[i] = 0.5/(instances.size()-trueCount);
			}
		}
		
	}
	
	private static List<List<Integer>> getSVMErroroutput(List<String> fileNames, List<Instances> instancesList, List<double[]> weightsList, List<Double> biasList) {
		InputStreamReader isr = null;
		List<List<Integer>> errorInstancePos = new ArrayList<List<Integer>>();
		
		for (String name : fileNames) {
			System.out.println("training file: " + name);
			try {
				isr = new InputStreamReader(new FileInputStream(name));
				Instances instances = new Instances(isr);
				
				instances.setClassIndex(instances.numAttributes()-1);
				SMO classifier = new SMO();
				classifier.setFilterType(new SelectedTag(2, SMO.TAGS_FILTER));
				classifier.setNumFolds(ConfigConstant.testFold);
				
				classifier.buildClassifier(instances);
				
				double[] wholeTrainWeights = classifier.sparseWeights()[0][1];
				double wholeTrainBias = classifier.bias()[0][1];
				List<Instances> kFoldInstances = new ArrayList<Instances>();
				int insCount = instances.size()/ConfigConstant.testFold;
				
				for (int i=0; i<ConfigConstant.testFold; i ++) {
					int count = insCount;
					int insStart = count*i;
					if (i==ConfigConstant.testFold-1) {
						count = instances.size()-count*i;
					}
					Instances subInstances = new Instances(instances, insStart, count);
					kFoldInstances.add(subInstances);
				}
				
				List<Integer> errorPoses = new ArrayList<Integer>();
				
//				int minError = Integer.MAX_VALUE;
				for (int i=0; i<kFoldInstances.size(); i ++) {
					Instances trainInstances =null;
					for (int j=0; j<kFoldInstances.size(); j ++) {
						if (i==j) {
							continue;
						}
						if (trainInstances == null) {
							trainInstances = new Instances(kFoldInstances.get(j));
						} else {
							trainInstances.addAll(kFoldInstances.get(j));
						}
					}
					classifier.buildClassifier(trainInstances);
					double[][][] weights = classifier.sparseWeights();
					double[][] bias = classifier.bias();
					
					List<Integer> partialErrorPos = testErrorPositions(kFoldInstances.get(i), weights[0][1], bias[0][1], i*insCount);
//					List<Integer> partialErrorPos = testErrorPositions(kFoldInstances.get(i), wholeTrainWeights, wholeTrainBias, i*insCount);
					errorPoses.addAll(partialErrorPos);
					
				}
				
				errorInstancePos.add(errorPoses);
				instancesList.add(instances);
				weightsList.add(wholeTrainWeights);
				biasList.add(wholeTrainBias);
				
				isr.close();
			} catch (Exception e) {
				System.out.println("exception on " + name);
				e.printStackTrace();
			}
		}
		return errorInstancePos;
	}
	
	private static List<Integer> testErrorPositions(Instances testInstances, double[] weights, Double bias, int foldCount) {
		int truePositiveCount = 0;
		int falsePositiveCount = 0;
		int trueNegativeCount = 0;
		int falseNegativeCount = 0;
		List<Integer> errorPoses = new ArrayList<Integer>();
		
		for (int j = 0; j < testInstances.size(); j++) {
			Instance ins = testInstances.instance(j);
			double result = 0;
			for (int k = 0; k<weights.length; k++) {
				result += ins.value(k)*weights[k];
			}
			result = result - bias;
			
			if (result<=0 && ins.classValue()==0) {truePositiveCount++;}
			if (result<=0 && ins.classValue()==1) 
			{falsePositiveCount++; errorPoses.add(foldCount+j);}
			if (result>0 && ins.classValue()==1) {trueNegativeCount++;}
			if (result>0 && ins.classValue()==0) 
			{falseNegativeCount++; errorPoses.add(foldCount+j);}
			
		}
		System.out.println("truePositiveCount: " + truePositiveCount);
		System.out.println("trueNegativeCount: " + trueNegativeCount);
		System.out.println("falseNegativeCount: " + falseNegativeCount);
		System.out.println("falsePositiveCount: " + falsePositiveCount);
		
		return errorPoses;
	}
	
}
