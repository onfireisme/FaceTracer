package face.feature.extraction;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class FaceRawDataGenerator {
	
	public void generateRawData() {
		Map<String, List<String>> sortedfileNames = getRawfileNames();
		
//		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.edgeMagnitudeAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.edgeMagnitudeMeanAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.edgeMagnitudeEnergyAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.edgeOrientationAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.intensityAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.intensityMeanAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.intensityEnergyAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.intensityHistogramAttributes);
//		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.RGBAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.RGBHistogramAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.RGBMeanAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.RGBEnergyAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.HSVAttributes);
		outputRawDataFile(sortedfileNames, ConfigConstant.Raw + ConfigConstant.HSVStatisticsAttributes);
	}
	
	private Map<String, List<String>> getRawfileNames() {
		File file=new File(ConfigConstant.rawScaleImagePath);
		String subPaths[];
		subPaths=file.list();
		
		Map<String, List<String>> sortedfileNames = new HashMap<String, List<String>>();
		for (String path : subPaths) {
			
			file=new File(ConfigConstant.rawScaleImagePath+path+"/");
			String[] names = file.list();
			List<String> fileNames = new ArrayList<String>();
			for (String name : names) {
				fileNames.add(ConfigConstant.rawScaleImagePath+path+"/"+name);
			}
			sortedfileNames.put(path ,fileNames);
		}
		return sortedfileNames;
	}
	
	private void outputRawDataFile(Map<String, List<String>> sortedfileNames, String outputFileName) {
		for (String path : sortedfileNames.keySet()) {
			
			try {
				BufferedImage img = ImageIO.read(new File(sortedfileNames.get(path).get(0)));
				CropFace.createDirectory(ConfigConstant.rawPath+path+"/");
				FileOutputStream fos = new FileOutputStream(ConfigConstant.rawPath+path+"/"+outputFileName);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.write("@relation predictor");
				bw.newLine();
				
				BufferedImage grayImg = FaceTrainDataGenerator.colorToGray(img);
				
				byte[] imgData = ((DataBufferByte)grayImg.getRaster().getDataBuffer()).getData();
				
				int imageLength = imgData.length;
				if (outputFileName.contains("Edge")) {
					int height = img.getHeight();
					int width = img.getWidth();
					imageLength = imageLength - height*2 - width*2 + 4;
				}
				
				FaceTrainDataGenerator.generateFileHeader(imageLength, bw, outputFileName);
				
				bw.write("@attribute faceId numeric");
				bw.newLine();
				bw.write("@data");
				bw.newLine();
				for (String name : sortedfileNames.get(path)) {
					int fid = Integer.valueOf(name.substring(name.lastIndexOf("/")+1, name.length()-4));
					
					FaceTrainDataGenerator.generateContent(imgData, name, outputFileName, bw);
					
					bw.write(fid+"");
					bw.newLine();
				}
				bw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		FaceRawDataGenerator fg = new FaceRawDataGenerator();
		fg.generateRawData();
	}
}
