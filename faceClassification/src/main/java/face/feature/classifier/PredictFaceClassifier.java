package face.feature.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;

import face.feature.extraction.ConfigConstant;

public class PredictFaceClassifier {
	public static void main(String[] args) {
		run();
	}
	
	public static void run() {
		File file = new File(ConfigConstant.rawPath);
		String[] subPaths = file.list();
		List<String> rawFileNames = new ArrayList<String>();
		for (String subPath :subPaths) {
			file = new File(ConfigConstant.rawPath+subPath+"/");
			String[] fileNames = file.list();
			for (String name : fileNames) {
				rawFileNames.add(ConfigConstant.rawPath+subPath+"/"+name);
			}
		}
		predictRawFaceData(rawFileNames, ConfigConstant.gender);
		predictRawFaceData(rawFileNames, ConfigConstant.mustache);
	}
	
	private static void predictRawFaceData(List<String> fileNames, String category) {
		InputStreamReader isr = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ConfigConstant.modelPath+"/"+category+"/"+ConfigConstant.dataModel));
			FaceClassifier fc = (FaceClassifier)ois.readObject();
			
			Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
			Map<Integer, Double> compareValueMap = new HashMap<Integer, Double>();
			List<Instances> instancesList = new ArrayList<Instances>();
			List<double[]> weightsList = fc.getWeightsList();
			List<Double> biasList = fc.getBiasList();
			List<Double> Bts = fc.getBts();
			List<Integer> classifierIds = fc.getClassifierIds();
			
			for (String name : fileNames) {
				isr = new InputStreamReader(new FileInputStream(name));
				Instances instances = new Instances(isr);
				instancesList.add(instances);
				
			}	
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
					int faceId = Double.valueOf(ins.value(ins.numAttributes()-1)).intValue();
					if (resultMap.keySet().contains(faceId)) {
						resultMap.put(faceId, resultMap.get(faceId)+result);
						compareValueMap.put(faceId, compareValueMap.get(faceId)+compareValue);
					} else {
						resultMap.put(faceId, result);
						compareValueMap.put(faceId, compareValue);
					}
				}
			}
			
			for (int i : resultMap.keySet()) {
				double result = resultMap.get(i);
				double compareValue = compareValueMap.get(i);
				if (result >= compareValue) {
					System.out.println(i+" " + category + ": " + ConfigConstant.attributeValueMap.get(category + ConfigConstant.B));
				} else {
					System.out.println(i+" " + category + ": " + ConfigConstant.attributeValueMap.get(category + ConfigConstant.A));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}