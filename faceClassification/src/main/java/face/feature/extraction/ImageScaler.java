package face.feature.extraction;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageScaler {
	
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
    			String distImgPath = ConfigConstant.scaleImagePath+faceRegion + name;
    			CropFace.createDirectory(ConfigConstant.scaleImagePath+faceRegion);
    			int[] size = ConfigConstant.faceRegionsSize.get(faceRegion);
    			resizeImage(srcImgPath, distImgPath, size[0], size[1]);
    		}
    	}
    }
    public static void main(String[] args) {
		batchResizeImage();
	}
}
