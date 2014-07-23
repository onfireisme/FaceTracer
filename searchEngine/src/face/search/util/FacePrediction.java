package face.search.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import config.ConfigConstant;
import face.search.bean.FaceIndex;


public class FacePrediction {
	public static void main(String[] args) {
		predictFace();
	}
	public static Map<String, String> predictFace() {
		cropUsingImageReader(ConfigConstant.testPic);
		batchResizeImage();
		FaceRawDataGenerator.generateRawData();
		return PredictFaceClassifier.predict();
	}
	
	private static void cropUsingImageReader(String name){ 
		 try {
        //image reader
        Iterator readers = ImageIO.getImageReadersByFormatName(ConfigConstant.imgSurffix); 
        ImageReader reader = (ImageReader)readers.next(); 

        //image input stream
        InputStream source=new FileInputStream(ConfigConstant.tempImagePath+name); 
        ImageInputStream iis = ImageIO.createImageInputStream(source); 
        reader.setInput(iis, true); 

        //image parameter 
        ImageReadParam param = reader.getDefaultReadParam(); 

        int imageIndex = 0; 

        int width = reader.getWidth(imageIndex); 

        int height = reader.getHeight(imageIndex); 
        FaceIndex faceData = getFacePoints();
        
        double [][] sixPoints = {{faceData.getLeft_eye_left_x(),faceData.getLeft_eye_right_x(), 
       	 					   faceData.getRight_eye_left_x(), faceData.getRight_eye_right_x(),
       	 					   faceData.getMouth_left_x(), faceData.getMouth_right_x()},
                                 {faceData.getLeft_eye_left_y(),faceData.getLeft_eye_right_y(), 
           	 				   faceData.getRight_eye_left_y(), faceData.getRight_eye_right_y(),
           	 				   faceData.getMouth_left_y(), faceData.getMouth_right_y()}};
        Double[][] keyPixels = AffineTransform.getKeyPixels(sixPoints);
        
        
        for (int i = 0; i < 11; i++) {
       	 if (keyPixels[i*2][0] < 0) {
       		 keyPixels[i*2][0] = 0d;
       	 }
       	 if (keyPixels[i*2+1][0]>= width) {
       		 keyPixels[i*2+1][0] = width-1.0;
       	 }
       	 if (keyPixels[i*2][1] < 0) {
       		 keyPixels[i*2][1] = 0d;
       	 }
       	 if (keyPixels[i*2+1][1] >= height) {
       		 keyPixels[i*2+1][1] = height-1.0;
       	 }
       	 
       	//image reader
            readers = ImageIO.getImageReadersByFormatName(ConfigConstant.imgSurffix); 
            reader = (ImageReader)readers.next(); 

            //image input stream
            source=new FileInputStream(ConfigConstant.tempImagePath+name); 
            iis = ImageIO.createImageInputStream(source); 
            reader.setInput(iis, true); 

            //image parameter 
            param = reader.getDefaultReadParam(); 
       	 
       	 int rectWidth = keyPixels[i*2+1][0].intValue() - keyPixels[i*2][0].intValue();
       	 int rectHeight = keyPixels[i*2+1][1].intValue() - keyPixels[i*2][1].intValue();
       	 if (rectWidth <= 0) {
       		 rectWidth = 10;
       	 }
       	 if (rectHeight <= 0) {
       		 rectHeight = 10;
       	 }
       	 Rectangle rect = new Rectangle(keyPixels[i*2][0].intValue(), keyPixels[i*2][1].intValue(), rectWidth, rectHeight); 
       	 
       	 param.setSourceRegion(rect); 
       	 
       	 BufferedImage bi = reader.read(0,param);
       	 switch (i) {
			 case 0:
				createDirectory(ConfigConstant.cropImagePath+ConfigConstant.wholeFace);
      		 	ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.wholeFace+name));
				break;
			 case 1:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.eye);
				 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.eye+name));
				 break;
			 case 2:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.mouth);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.mouth+name));
				 break;
			 case 3:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.hair);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.hair+name));
				 break;
			 case 4:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.nose);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.nose+name));
				 break;
			 case 5:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.cheeks);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.cheeks+name));
				 break;
			 case 6:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.chin);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.chin+name));
				 break;
			 case 7:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.forehead);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.forehead+name));
				 break;
			 case 8:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.eyebrows);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.eyebrows+name));
			 case 9:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.upperLip);
       		 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.upperLip+name));
				 break;
			 case 10:
				 createDirectory(ConfigConstant.cropImagePath+ConfigConstant.background);
				 ImageIO.write(bi, ConfigConstant.imgSurffix, new File(ConfigConstant.cropImagePath+ConfigConstant.background+name));
				 break;
			 default:
				break;
			 }
       	 
       	 iis.close();
       	 source.close();
        }
        System.out.println("crop image success!");
        
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(name + " exception!");
			e.printStackTrace();
		}
 }
	
	private static FaceIndex getFacePoints() {
		String s = null;
		int count = 0;
		try {
			FileInputStream fis = new FileInputStream(ConfigConstant.landMarkPath);
			InputStreamReader ir = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(ir);
			
			while ((s= br.readLine()) != null) {
				count++;
				String[] contents = s.split("  ");
				String faceID = contents[0];
				FaceIndex fif = new FaceIndex();
				fif.setId(faceID);
				fif.setLeft_eye_left_x(Double.valueOf(contents[70]).intValue());
				fif.setLeft_eye_left_y(Double.valueOf(contents[71]).intValue());
				fif.setLeft_eye_right_x(Double.valueOf(contents[62]).intValue());
				fif.setLeft_eye_right_y(Double.valueOf(contents[63]).intValue());
				fif.setRight_eye_left_x(Double.valueOf(contents[82]).intValue());
				fif.setRight_eye_left_y(Double.valueOf(contents[83]).intValue());
				fif.setRight_eye_right_x(Double.valueOf(contents[90]).intValue());
				fif.setRight_eye_right_y(Double.valueOf(contents[91]).intValue());
				fif.setMouth_left_x(Double.valueOf(contents[120]).intValue());
				fif.setMouth_left_y(Double.valueOf(contents[121]).intValue());
				fif.setMouth_right_x(Double.valueOf(contents[132]).intValue());
				fif.setMouth_right_y(Double.valueOf(contents[133]).intValue());
				return fif;
			}
		} catch (Exception e) {
			System.out.println(s);
			System.out.println("line count:"+count);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void createDirectory(String path){
		 File directory = new File(path);
		 if (!directory.exists()) {
			directory.mkdirs();
		 }
	 }
	
    public static void resizeImage(String srcImgPath, String distImgPath,   
            int width, int height) {   
  
        try {
			File sourceFile = new File(srcImgPath);   
			Image sourceImg = ImageIO.read(sourceFile);   
			BufferedImage buffImg = null;   
			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
			buffImg.getGraphics().drawImage(   
			        sourceImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,   
			        0, null);   
  
			ImageIO.write(buffImg, ConfigConstant.imgSurffix, new File(distImgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}   
  
    }
    
    public static void batchResizeImage() {
    	for (String faceRegion: ConfigConstant.faceRegions) {
    		File srcPath = new File(ConfigConstant.cropImagePath+faceRegion);
    		String[] fileNames = srcPath.list();
    		for (String name : fileNames) {
    			String srcImgPath = ConfigConstant.cropImagePath+faceRegion + name;
    			String distImgPath = ConfigConstant.rawScaleImagePath+faceRegion + name;
    			createDirectory(ConfigConstant.rawScaleImagePath+faceRegion);
    			int[] size = ConfigConstant.faceRegionsSize.get(faceRegion);
    			resizeImage(srcImgPath, distImgPath, size[0], size[1]);
    		}
    	}
    }
}
