package face.tracer.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import face.feature.bean.FaceIndex;
import face.feature.extraction.ConfigConstant;

public class Statistic {
	public static void main(String[] args) {
		File file=new File(ConfigConstant.scaleImagePath);
		String path = file.list()[0];
		file=new File(ConfigConstant.scaleImagePath+path+"/");
		String[] trainData = file.list();
		List<FaceIndex> flist = readData();
		List<String> dataNames = new ArrayList<String>();
		for (String data:trainData) {
			dataNames.add(data);
		}
		Map<String, Integer> statiticMap = new HashMap<String, Integer>();
		Map<String, Integer> ageMap = new HashMap<String, Integer>();
		for (FaceIndex fi : flist) {
			if (dataNames.contains(fi.getId()+"."+ConfigConstant.imgSurffix)){
			if (statiticMap.containsKey(fi.getAttribute())) {
				statiticMap.put(fi.getAttribute(), statiticMap.get(fi.getAttribute())+1);
			} else {
				statiticMap.put(fi.getAttribute(), 1);
			}
			if ("age".equals(fi.getAttribute())) {
				if (ageMap.containsKey(fi.getLabel())) {
					ageMap.put(fi.getLabel(), ageMap.get(fi.getLabel())+1);
				} else {
					ageMap.put(fi.getLabel(), 1);
				}
			} 
			}
		}
		System.out.println(statiticMap);
		System.out.println(ageMap);
	}
	
	public static List<FaceIndex> readData(){
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		String s = null;
		
		try {
			FileInputStream fis = new FileInputStream(ConfigConstant.path+"facelabels20140718_10.txt");
			InputStreamReader ir = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(ir);
			while((s=br.readLine())!=null)
			{
				if (s.startsWith("#")) {
					continue;
				}
				
				String[] contents = s.split("\t");
				
					FaceIndex fi = new FaceIndex();
					fi.setId(contents[0]);
					fi.setAttribute(contents[1]);
					fi.setLabel(contents[2]);
					filist.add(fi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filist;
	}
}
