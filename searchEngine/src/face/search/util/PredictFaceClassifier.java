package face.search.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;
import config.ConfigConstant;
import face.feature.classifier.FaceClassifier;
import face.search.bean.MultiClassInstance;

public class PredictFaceClassifier {
	public static void main(String[] args) {
		predict();
	}
	
	public static Map<String, String> predict() {
		File file = new File(ConfigConstant.rawPath);
		String[] subPaths = file.list();
		List<String> rawFileNames = new ArrayList<String>();
		Map<String, String> predictResult = new HashMap<String, String>();
		for (String subPath :subPaths) {
			file = new File(ConfigConstant.rawPath+subPath+"/");
			String[] fileNames = file.list();
			for (String name : fileNames) {
				rawFileNames.add(ConfigConstant.rawPath+subPath+"/"+name);
			}
		}
		for (String category : ConfigConstant.attributeMap.keySet()) {
			if (ConfigConstant.multiClassAttrs.contains(category)) {
				continue;
			}
			predictRawFaceData(rawFileNames, category, predictResult);
		}
		
		for (String category : ConfigConstant.multiClassLabels) {
			predictMultiClassRawFaceData(rawFileNames, category, predictResult);
		}
		return predictResult;
	}
	
	private static void predictRawFaceData(List<String> fileNames, String category, Map<String, String> predictResult) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ConfigConstant.modelPath+"/"+category+"/"+ConfigConstant.dataModel));
			FaceClassifier fc = (FaceClassifier)ois.readObject();
			
			Map<String, Double> resultMap = new HashMap<String, Double>();
			Map<String, Double> compareValueMap = new HashMap<String, Double>();
			List<Instances> instancesList = new ArrayList<Instances>();
			List<double[]> weightsList = fc.getWeightsList();
			List<Double> biasList = fc.getBiasList();
			List<Double> Bts = fc.getBts();
			List<Integer> classifierIds = fc.getClassifierIds();
			
			instancesList = getNormalizedInstances(fileNames);
			
			for (int i = 0;i < classifierIds.size(); i++) {
				int cid = classifierIds.get(i);
				double Bt = Bts.get(i);
				double[] weights = weightsList.get(cid);
				double  bias = biasList.get(cid); 
				Instances instances = instancesList.get(cid);
				for (Instance ins : instances) {
					double SVMresult = 0;
					double cResult = 0;
					for (int j = 0; j < weights.length; j++) {
						SVMresult += ins.value(j)*weights[j];
					}
					SVMresult -=bias;
					if (SVMresult > 0) {
						cResult = 1;
					}
							
					double result = Math.log10(1/Bt)*cResult;
					double compareValue =Math.log10(1/Bt)*0.5;
					String faceId = ins.stringValue(ins.numAttributes()-1);
					if (resultMap.keySet().contains(faceId)) {
						resultMap.put(faceId, resultMap.get(faceId)+result);
						compareValueMap.put(faceId, compareValueMap.get(faceId)+compareValue);
					} else {
						resultMap.put(faceId, result);
						compareValueMap.put(faceId, compareValue);
					}
				}
			}
			
			for (String faceId : resultMap.keySet()) {
				double result = resultMap.get(faceId);
				double compareValue = compareValueMap.get(faceId);
				String str = null;
				String value = null;
				if (result >= compareValue) {
					value = ConfigConstant.attributeValueMap.get(category + ConfigConstant.B);
					str = faceId+" " + category + ": " + value;
					System.out.println(str);
					predictResult.put(category, value);
				} else {
					value = ConfigConstant.attributeValueMap.get(category + ConfigConstant.A);
					str = faceId+" " + category + ": " + value;
					System.out.println(str);
					predictResult.put(category, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static List<Instances> getNormalizedInstances(List<String> fileNames) {
		InputStreamReader isr = null;
		List<Instances> instancesList = new ArrayList<Instances>();
		try {
			for (String name : fileNames) {
				isr = new InputStreamReader(new FileInputStream(name));
				Instances instances = new Instances(isr);
				instancesList.add(instances);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instancesList;
	}
	
	private static void predictMultiClassRawFaceData(List<String> fileNames, String category, Map<String, String> predictResult) {
		List<String> classValues = ConfigConstant.multiClassAttributeValueMap.get(category);
		List<MultiClassInstance> multiClassInstances = new ArrayList<MultiClassInstance>();
		MultiClassInstance multiCins = new MultiClassInstance();
		
		List<Instances> instancesList = new ArrayList<Instances>();
		try {
			instancesList = getNormalizedInstances(fileNames);
		
		
		for (String classValue : classValues) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ConfigConstant.modelPath+"/"+classValue+"/"+ConfigConstant.dataModel));
			FaceClassifier fc = (FaceClassifier)ois.readObject();
			
			Map<String, Double> svmValueMap = new HashMap<String, Double>();
			List<double[]> weightsList = fc.getWeightsList();
			List<Double> biasList = fc.getBiasList();
			List<Double> Bts = fc.getBts();
			List<Integer> classifierIds = fc.getClassifierIds();
			
			for (int i = 0;i < classifierIds.size(); i++) {
				int cid = classifierIds.get(i);
				double Bt = Bts.get(i);
				double[] weights = weightsList.get(cid);
				double  bias = biasList.get(cid); 
				Instances instances = instancesList.get(cid);
				for (Instance ins : instances) {
					double SVMresult = 0;
					for (int j = 0; j < weights.length; j++) {
						SVMresult += ins.value(j)*weights[j];
					}
					SVMresult -=bias;
					
					double weightedSVM = Math.log10(1/Bt)* SVMresult;
					String faceId = ins.stringValue(ins.numAttributes()-1);
					if (svmValueMap.keySet().contains(faceId)) {
						svmValueMap.put(faceId, svmValueMap.get(faceId)+weightedSVM);
					} else {
						svmValueMap.put(faceId, weightedSVM);
					}
				}
			}
			
			for (String faceId : svmValueMap.keySet()) {
				double svmValue = svmValueMap.get(faceId);
				
				MultiClassInstance multiIns = null;
				multiCins.setId(faceId);
				if (multiClassInstances.contains(multiCins)) {
					multiIns = multiClassInstances.get(multiClassInstances.indexOf(multiCins));
					if (multiIns.getValue() > svmValue) {
						multiIns.setValue(svmValue);
						multiIns.setClassValue(classValue);
					}
				} else {
					multiIns = new MultiClassInstance();
					multiIns.setId(multiCins.getId());
					multiIns.setValue(svmValue);
					multiIns.setClassValue(classValue);
					multiClassInstances.add(multiIns);
				}
			}
		}
		
		for (MultiClassInstance mulIns : multiClassInstances) {
			System.out.println(mulIns.getId()+" " + category + ": " + mulIns.getClassValue());
			predictResult.put(category, mulIns.getClassValue());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static Instances normalizeInstances(Instances instances) {
//		Filter m_Filter = new Normalize();
//		try {
//			m_Filter.setInputFormat(instances);
//			Instances res = Filter.useFilter(instances, m_Filter);
//			return res;
//		} catch (Exception e) {
//			System.out.println("Normalization Error!");
//			e.printStackTrace();
//		} 
//		return instances;
//	}
	
}