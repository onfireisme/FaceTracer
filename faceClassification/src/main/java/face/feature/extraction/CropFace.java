package face.feature.extraction;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import face.feature.bean.FaceIndex;

public class CropFace {
	 
	public void batchCorpFaces() {
		File file=new File(ConfigConstant.testImagePath);
		String fileNames[];
		fileNames=file.list();
		
		for (String name : fileNames) {
			cropUsingImageReader(name);
		}
	}
	
	 public void cropUsingImageReader(String name){ 
		 try {
         //image reader
         Iterator readers = ImageIO.getImageReadersByFormatName(ConfigConstant.imgSurffix); 
         ImageReader reader = (ImageReader)readers.next(); 

         //image input stream
         InputStream source=new FileInputStream(ConfigConstant.testImagePath+name); 
         ImageInputStream iis = ImageIO.createImageInputStream(source); 
         reader.setInput(iis, true); 

         //image parameter 
         ImageReadParam param = reader.getDefaultReadParam(); 

         int imageIndex = 0; 

         int width = reader.getWidth(imageIndex); 

         int height = reader.getHeight(imageIndex); 
         List<FaceIndex> filist = FaceFeaturePointsExtraction.getFaceDataInstance();
         String faceId = name.substring(0,name.length()-4);
         FaceIndex faceData = null;
         for (FaceIndex fi : filist) {
        	 if (fi.getId().equals(faceId)) {
        		 faceData = fi;
        		 break;
        	 }
         }
         if (faceData == null) {
        	 return;
         }
         
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
             source=new FileInputStream(ConfigConstant.testImagePath+name); 
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
         System.out.println("success!"+ faceId);
         
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(name + " exception!");
			e.printStackTrace();
		}
  } 
	 
	 public static void createDirectory(String path){
		 File directory = new File(path);
		 if (!directory.exists()) {
			directory.mkdirs();
		 }
	 }
	 
	 public static void main(String[] args) {
		 CropFace cropFace = new CropFace();
		 cropFace.batchCorpFaces();
		
	}
}
