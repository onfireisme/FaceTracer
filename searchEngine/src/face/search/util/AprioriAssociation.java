package face.search.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import config.ConfigConstant;
import face.search.bean.PhotoEvalInfo;
import face.search.db.MongoDBUtil;

public class AprioriAssociation {
	public static void main(String[] args) {
		generateFile();
	}
	
	private static void generateFile() {
		try {
			FileOutputStream fos = new FileOutputStream(ConfigConstant.path+config.ConfigConstant.picEvaluationFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write("@relation "+"evaluation");
			bw.newLine();
			bw.write("@attribute "+"transID {");
			for (int i = 1; i <=300; i++) {
				if (i != 300) {
					bw.write(i+",");
				} else {
					bw.write(i+"");
				}
			}
			bw.write("}");
			bw.newLine();
			bw.write("@attribute Item {");
			int count = 0;
			for (String classValue: ConfigConstant.classValueList) {
				count++;
				if (count!=ConfigConstant.classValueList.size()) {
					bw.write(classValue+",");
				} else {
					bw.write(classValue);
				}
			}
			bw.write("}");
			bw.newLine();
			bw.write("@data");
			bw.newLine();
			
			List<PhotoEvalInfo> data = MongoDBUtil.getEvaluatedPhotos();
			
			count = 1;
			for (PhotoEvalInfo pei : data) {
				bw.write(count+","+ConfigConstant.age+":"+pei.getAge());
				bw.newLine();
				bw.write(count+","+ConfigConstant.blurry+":"+pei.getBlurry());
				bw.newLine();
				bw.write(count+","+ConfigConstant.environment+":"+pei.getEnvironment());
				bw.newLine();
				bw.write(count+","+ConfigConstant.eye_wear+":"+pei.getEye_wear());
				bw.newLine();
				bw.write(count+","+ConfigConstant.gender+":"+pei.getGender());
				bw.newLine();
				bw.write(count+","+ConfigConstant.hair_color+":"+pei.getHair_color());
				bw.newLine();
				bw.write(count+","+ConfigConstant.lighting+":"+pei.getLighting());
				bw.newLine();
				bw.write(count+","+ConfigConstant.mustache+":"+pei.getMustache());
				bw.newLine();
				bw.write(count+","+ConfigConstant.race+":"+pei.getRace());
				bw.newLine();
				bw.write(count+","+ConfigConstant.smiling+":"+pei.getSmiling());
				bw.newLine();
				count++;
			}
			
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
