package face.feature.extraction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DealMismatch {
	public static void main(String[] args) {
			File file=new File(ConfigConstant.testImagePath);
			String fileNames[];
			fileNames=file.list();
			
			File matchedWholeFace = new File(ConfigConstant.cropImagePath+ConfigConstant.wholeFace);
			String matchedNames[];
			matchedNames = matchedWholeFace.list();
			List<String> matchedList = new ArrayList<String>();
			for (String name : matchedNames) {
				matchedList.add(name);
			}
			for (String name : fileNames) {
				if (!matchedList.contains(name)) {
					try {
						File mismatchFile = new File(ConfigConstant.testImagePath+name);
						BufferedImage img = ImageIO.read(mismatchFile);
						if (img == null) {
							continue;
						}
						ImageIO.write(img, ConfigConstant.imgSurffix, new File(ConfigConstant.misMatchPicPath+name));
						mismatchFile.delete();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
}
