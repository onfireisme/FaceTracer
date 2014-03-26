package face.feature.extraction;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class FaceDataProcess {
	
	
	public static List<FaceIndex> getValidFiles() {
		
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		List<FaceIndex> validFaceIndexes = new ArrayList<FaceIndex>(); 
//		int maleCount = 0;
//		int femaleCount = 0;
		
		String s = null;
		try {
			FileInputStream fis = new FileInputStream(ConfigConstant.path+ConfigConstant.faceIndexName);
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
				fi.setUrl(contents[1]);
				filist.add(fi);
	        }
			
			fis = new FileInputStream(ConfigConstant.path+ConfigConstant.faceStatsName);
			ir = new InputStreamReader(fis);
			br = new BufferedReader(ir);
			while ((s=br.readLine())!=null) {
				if (s.startsWith("#")) {
	        		continue;
				}
				String[] contents = s.split("\t");
				int fid = Integer.valueOf(contents[0]);
				FaceIndex fif = filist.get(fid-1);
				fif.setYaw(Integer.valueOf(contents[5]));
				fif.setPitch(Integer.valueOf(contents[6]));
				fif.setRoll(Integer.valueOf(contents[7]));
			}
			
			FileInputStream fisfl = new FileInputStream(ConfigConstant.path+ConfigConstant.faceLabelName);
			ir = new InputStreamReader(fisfl);
			br = new BufferedReader(ir);
			FaceIndex fif = new FaceIndex();
			
			while((s=br.readLine())!=null)
	        {
	        	if (s.startsWith("#")) {
	        		continue;
	        	}
	        	String[] contents = s.split("\t");
//	        	if (!contents[1].equals("gender")) {
//	        		continue;
//	        	}
//	        	if (contents[2].equals("male")&&maleCount>=50) {
//	        		continue;
//	        	} 
//	        	if (contents[2].equals("female")&&femaleCount>=50) {
//	        		continue;
//	        	}
	        	int id = Integer.valueOf(contents[0]);
	        	
	        	fif = filist.get(id-1);
	        	if ((fif.getYaw()>10||fif.getYaw()<-10)||
	        		(fif.getPitch()>10||fif.getPitch()<-10)||
	        		(fif.getRoll()>10||fif.getRoll()<-10)) {
	        		continue;
	        	}
	        	if (id == 8958) {
	        		continue;
	        	}
	        	if (fif.getUrl().startsWith("http://imstars.aufeminin")) {
	        		continue;
	        	}
	        	fif.setAttribute(contents[1]);
	        	fif.setLabel(contents[2]);
	        	BufferedImage img = null;
	        	
	        	try {
					img = ImageIO.read(new URL(fif.getUrl()));
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	if (img!=null) {
	        		ImageIO.write(img, ConfigConstant.imgSurffix, new File(ConfigConstant.testImagePath+fif.getId()+"."+ConfigConstant.imgSurffix));
	        		validFaceIndexes.add(fif);
//	        		if (contents[2].equals("male")) {
//	        			maleCount++;
//	        		}
//	        		if (contents[2].equals("female")) {
//	        			femaleCount++;
//	        		}
	        	}
//	        	if (validFaceIndexes.size()>=100) {
//	        		break;
//	        	}
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return validFaceIndexes;
	} 
	
	public static void main(String[] args) {
		List<FaceIndex> filist = getValidFiles();
		try {
			FileOutputStream fos = new FileOutputStream(ConfigConstant.path+ConfigConstant.faceDataName);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write("#faceID\tURL\tAttritube\tLabel");
			bw.newLine();
			for (FaceIndex fi: filist) {
				bw.write(fi.getId()+"\t"+fi.getUrl()+"\t"+fi.getAttribute()+"\t"+fi.getLabel());
				bw.newLine();
			}
			bw.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
