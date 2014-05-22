package face.feature.extraction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class FaceTrainDataGenerator {
	
	
	public List<FaceIndex> readFaceLables () {
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		String s = null;
		
		try {
			FileInputStream fis = new FileInputStream(ConfigConstant.path+ConfigConstant.faceLabelName);
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
	
	private Map<String, List<String>> getTrainfileNames() {
		File file=new File(ConfigConstant.scaleImagePath);
		String subPaths[];
		subPaths=file.list();
		
		Map<String, List<String>> sortedfileNames = new HashMap<String, List<String>>();
		for (String path : subPaths) {
			if (path.equals("raw")) {
				continue;
			}
			file=new File(ConfigConstant.scaleImagePath+path+"/");
			String[] names = file.list();
			List<String> fileNames = new ArrayList<String>();
			for (String name : names) {
				fileNames.add(ConfigConstant.scaleImagePath+path+"/"+name);
			}
			sortedfileNames.put(path ,fileNames);
		}

		return sortedfileNames;
	}
	
	public void writeTrainingFile(List<FaceIndex> filist) {
		Map<String, List<String>> sortedfileNames =getTrainfileNames();
		
		for (String category : ConfigConstant.attributeMap.keySet()) {
//			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.edgeMagnitudeAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.edgeMagnitudeMeanAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.edgeMagnitudeEnergyAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.edgeOrientationAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.intensityAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.intensityMeanAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.intensityEnergyAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.intensityHistogramAttributes, category);
//			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.RGBAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.RGBMeanAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.RGBEnergyAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.HSVAttributes, category);
			outputAttributFile(filist, sortedfileNames, ConfigConstant.Train + ConfigConstant.HSVStatisticsAttributes, category);
		}
	}
	
	private void outputAttributFile(List<FaceIndex> filist, Map<String, List<String>> sortedfileNames, String outputFileName, String category) {
		
		for (String path:sortedfileNames.keySet()) { 
			
			try {
				BufferedImage img = ImageIO.read(new File(sortedfileNames.get(path).get(0)));
				CropFace.createDirectory(ConfigConstant.trainPath+ path +"/" +category+"/");
				FileOutputStream fos = new FileOutputStream(ConfigConstant.trainPath+ path +"/" +category+"/"+ category + outputFileName);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.write("@relation "+category);
				bw.newLine();
				
				BufferedImage grayImg = colorToGray(img);
				
				byte[] imgData = ((DataBufferByte)grayImg.getRaster().getDataBuffer()).getData();
				
				int imageLength = imgData.length;
				if (outputFileName.contains("Edge")) {
					int height = img.getHeight();
					int width = img.getWidth();
					imageLength = imageLength - height*2 - width*2 + 4;
				}
				
				generateFileHeader(imageLength, bw, outputFileName);
				
				bw.write("@attribute "+ConfigConstant.attributeMap.get(category));
				bw.newLine();
				bw.write("@data");
				bw.newLine();
				for (String name : sortedfileNames.get(path)) {
					String fid = name.substring(name.lastIndexOf("/")+1, name.length()-4);
					FaceIndex faceIndex = null;
					for (FaceIndex fi : filist) {
						if (fi.id.equals(fid)&& fi.attribute.equals(category)) {
							faceIndex = fi;
							break;
						}
					}
					if (faceIndex==null) {
						continue;
					}
					
					generateContent(imgData, name, outputFileName, bw);
					
					bw.write(faceIndex.getLabel());
					bw.newLine();
				}
				bw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void generateFileHeader(int imageLength, BufferedWriter bw, String outputFileName) throws Exception {
		if (outputFileName.contains("Intensity")
				&& outputFileName.contains("Histogram")) {
			for (int i = 0; i < 256; i++) {
				bw.write("@attribute " + (i + 1) + "level numeric");
				bw.newLine();
			}
		} else if (outputFileName.contains("Statistics")) {
			if (outputFileName.contains("HSV")) {
				bw.write("@attribute hue_mean numeric");
				bw.newLine();
				bw.write("@attribute saturation_mean numeric");
				bw.newLine();
				bw.write("@attribute value_mean numeric");
				bw.newLine();
				bw.write("@attribute hue_deviation numeric");
				bw.newLine();
				bw.write("@attribute saturation_deviation numeric");
				bw.newLine();
				bw.write("@attribute value_deviation numeric");
				bw.newLine();
			} else {
				bw.write("@attribute mean numeric");
				bw.newLine();
				bw.write("@attribute standard_deviation numeric");
				bw.newLine();
			}
			bw.newLine();
		} else if (outputFileName.contains("HSV")) {
			for (int i = 0; i < imageLength; i++) {
				bw.write("@attribute " + (i + 1) + "pixel_hue numeric");
				bw.newLine();
				bw.write("@attribute " + (i + 1) + "pixel_saturation numeric");
				bw.newLine();
				bw.write("@attribute " + (i + 1) + "pixel_value numeric");
				bw.newLine();
			}
		} else {
			for (int i = 0; i < imageLength; i++) {
				bw.write("@attribute " + (i + 1) + "pixel numeric");
				bw.newLine();
			}
		}
	}
	
	public static void generateContent(byte[] imgData, String name, String outputFileName, BufferedWriter bw) throws Exception {
		BufferedImage img = ImageIO.read(new File(name));
		int[] pixelValues = new int[imgData.length];
		double[] hue = new double[imgData.length];
		double[] saturation = new double[imgData.length];
		double[] value = new double[imgData.length];
		int subImgLen = imgData.length - 2*img.getHeight() - 2*img.getWidth() + 4;
		double[] magnitudes = new double[subImgLen];
		double[] orientations = new double[subImgLen];

		BufferedImage grayImg = colorToGray(img);
		imgData = ((DataBufferByte) grayImg.getRaster().getDataBuffer()).getData();
		if (outputFileName.contains("Intensity")) {
			copyImageData(pixelValues, imgData);
		} else if (outputFileName.contains("RGB")) {
			pixelValues = getImageGRB(img);
		} else if (outputFileName.contains("HSV")) {
			setImageHSV(img, hue, saturation, value);
		} else if (outputFileName.contains("Edge")) {
			setMagnitudeOrientation(imgData, img.getWidth(), magnitudes, orientations);
		}
		
		double[] outputValue = null;

		if (outputFileName.contains("HSV")) {
			outputValue = new double[imgData.length * 3];
			for (int i = 0; i < imgData.length; i++) {
				outputValue[i * 3] = hue[i];
				outputValue[i * 3 + 1] = saturation[i];
				outputValue[i * 3 + 2] = value[i];
			}
		} else if (outputFileName.contains("Edge")) {
			outputValue = new double[subImgLen];
			if (outputFileName.contains("Magnitude")) {
				for (int i = 0; i < subImgLen; i++) {
					outputValue[i] = magnitudes[i];
				}
			} else {
				for (int i = 0; i < subImgLen; i++) {
					outputValue[i] = orientations[i];
				}
			}
		} else {
			outputValue = new double[imgData.length];
			for (int i = 0; i < pixelValues.length; i++) {
				outputValue[i] = pixelValues[i];
			}
		}
		double mean = getAverage(outputValue);
		double standardDeviation = getStandardDeviation(outputValue);

		double hue_mean = getAverage(hue);
		double saturation_mean = getAverage(saturation);
		double value_mean = getAverage(value);

		double hue_sd = getStandardDeviation(hue);
		double saturation_sd = getStandardDeviation(saturation);
		double value_sd = getStandardDeviation(value);

		if (outputFileName.contains("HSV") && outputFileName.contains("Mean")) {
			for (int i = 0; i < hue.length; i++) {
				if (hue_mean != 0)
					outputValue[i * 3] = hue[i] / hue_mean;
				if (saturation_mean != 0)
					outputValue[i * 3 + 1] = saturation[i] / saturation_mean;
				if (value_mean != 0)
					outputValue[i * 3 + 2] = value[i] / value_mean;
			}
		} else if (outputFileName.contains("Mean")) {
			for (int i = 0; i < outputValue.length; i++) {
				if (mean != 0)
					outputValue[i] = outputValue[i] / mean;
			}
		}

		if (outputFileName.contains("HSV") && outputFileName.contains("Energy")) {
			for (int i = 0; i < hue.length; i++) {
				if (hue_sd != 0)
					outputValue[i * 3] = (hue[i] - hue_mean) / hue_sd;
				if (saturation_sd != 0)
					outputValue[i * 3 + 1] = (saturation[i] - saturation_mean)
							/ saturation_sd;
				if (value_sd != 0)
					outputValue[i * 3 + 2] = (value[i] - value_mean) / value_sd;
			}
		} else if (outputFileName.contains("Energy")) {
			if (standardDeviation != 0) {
				for (int i = 0; i < outputValue.length; i++) {
					outputValue[i] = (outputValue[i] - mean) / standardDeviation;
				}
			}
		}
		if (outputFileName.contains("Intensity")
				&& outputFileName.contains("Histogram")) {
			outputValue = histogram(outputValue);
		}
		if (outputFileName.contains("Statistics")) {
			if (outputFileName.contains("HSV")) {
				outputValue = new double[6];
				outputValue[0] = hue_mean;
				outputValue[1] = saturation_mean;
				outputValue[2] = value_mean;
				outputValue[3] = hue_sd * hue_sd;
				outputValue[4] = saturation_sd * saturation_sd;
				outputValue[5] = value_sd * value_sd;
			} else {
				outputValue = new double[2];
				outputValue[0] = mean;
				outputValue[1] = standardDeviation * standardDeviation;
			}
		}

		for (int i = 0; i < outputValue.length; i++) {
			bw.write(outputValue[i] + ",");
		}
	}
	
	public static double[] histogram (double[] outputValue) {
		double[] returnValue = new double[256];
		for (int i=0;i<outputValue.length;i++) {
			int value = Double.valueOf(outputValue[i]).intValue();
			returnValue[value]++;
		}
		return returnValue;
	}
	
	public static void copyImageData(int[] pixelValues, byte[] imgData) {
		for (int i = 0; i < imgData.length; i ++) {
			pixelValues[i] = (imgData[i]& 0xFF);
		}
	}
	
	public static int[] getImageGRB(BufferedImage img) {
		int[] result = null;
		
		int height = img.getHeight();
		int width = img.getWidth();
		result = new int[width*height];
		int count = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				result[count] = img.getRGB(i, j) & 0xFFFFFF;
				count++;
			}
		}
		
		return result;
	}
	
	public static void setImageHSV(BufferedImage img, double[] hue, double[] saturation, double[] value) {
		int height = img.getHeight();
		int width = img.getWidth();
		int count = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = img.getRGB(i, j) & 0xFFFFFF; 
				Color c = new Color(rgb);
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				float[] hsv = new float[3];
				Color.RGBtoHSB(r, g, b, hsv);
				hue[count] = hsv[0];
				saturation[count] = hsv[1];
				value[count] = hsv[2];
				count++;
			}
        }
  }
	
	//get mean
    public static double getAverage(double[] array){
    	int num = array.length;
        double sum = 0;
        for(int i = 0;i < num;i++){
            sum += array[i];
        }
        return sum / num;
    }
   
    //get standard deviation
    public static double getStandardDeviation(double[] array){
    	int num = array.length;
        double sum = 0;
        double mean = getAverage(array);
        for(int i = 0;i < num;i++){
            sum += (array[i] - mean) * (array[i] - mean);
        }
        return Math.sqrt((sum / num));
    }
	
	public static BufferedImage colorToGray(BufferedImage source) {
        BufferedImage returnValue = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = returnValue.getGraphics();
        g.drawImage(source, 0, 0, null);
        return returnValue;
    }
	
	public static void setMagnitudeOrientation(byte[] img, int w, double[] magnitudes, double[] orientations) {
		int count = 0;
		for (int pos = w; pos < img.length; pos++) {
			if (pos - w >= 0&& pos + w < img.length && pos%w != 0 && pos%w != w-1) {
				double dy = (img[pos - w]& 0xFF) - (img[pos + w]& 0xFF);
				double dx = (img[pos - 1]& 0xFF) - (img[pos + 1]& 0xFF);
				magnitudes[count] = Math.sqrt(dx*dx + dy*dy);
				if (dx == 0) {
					dx = 1;
				} 
				orientations[count] = Math.atan2(dy, dx);
				count ++;
			}
		}
	}
	
	public static void run() {
		FaceTrainDataGenerator fg = new FaceTrainDataGenerator();
		List<FaceIndex> filist = fg.readFaceLables();
		fg.writeTrainingFile(filist);
	}
	
	public static void main(String[] args) {
		FaceTrainDataGenerator fg = new FaceTrainDataGenerator();
		List<FaceIndex> filist = fg.readFaceLables();
		fg.writeTrainingFile(filist);
	}
}
