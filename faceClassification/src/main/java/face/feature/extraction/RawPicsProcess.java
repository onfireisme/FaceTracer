package face.feature.extraction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class RawPicsProcess {
	private static String[] picNams = {};//{"1.bmp","691.bmp","11446.bmp","11443.bmp","14102.bmp","14876.bmp","14443.bmp"};
	
	private static void copyPics(){
		File file=new File(ConfigConstant.scaleImagePath);
		String subPaths[];
		subPaths=file.list();
		String extractPath = subPaths[0];
		file=new File(ConfigConstant.scaleImagePath+extractPath+"/");
		String[] names = file.list();
		List<String> copyPics = new ArrayList<String>();
		for (String name : picNams) {
			copyPics.add(name);
		}
		for (int i=0; i < 500; i++) {
			int ran = 0;
			while (copyPics.contains(names[ran])) {
				ran = new Random().nextInt(names.length);
			}
			copyPics.add(names[ran]);
		}
		
		for (String path : subPaths) {
			if ("raw".equals(path)) {
				continue;
			}
			for (String picNam : copyPics) {
				try {
					File copyFile = new File(ConfigConstant.scaleImagePath+path+"/"+picNam);
					BufferedImage img = ImageIO.read(copyFile);
					ImageIO.write(img, ConfigConstant.imgSurffix, new File(ConfigConstant.rawScaleImagePath+path+"/"+picNam));
					} catch (IOException e) {
						System.out.println("exception!");
						System.out.println("input: "+ConfigConstant.scaleImagePath+path+"/"+picNam);
						System.out.println("output: "+ConfigConstant.rawScaleImagePath+path+"/"+picNam);
						e.printStackTrace();
					}
				}
			}
	}
	
	public static void main(String[] args) {
		copyPics();
	}
}
