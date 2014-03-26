package face.feature.extraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FaceDataReader {
	
	private static List<FaceIndex> filist = null;
	
	public static List<FaceIndex> getFaceDataInstance() {
		if (filist == null) {
			filist = getFaceBasicInfo();
		}
		return filist;
	}
	
	private static List<FaceIndex> getFaceBasicInfo() {
		
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		
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
					fif.setLeft_eye_left_x(Integer.valueOf(contents[8]));
					fif.setLeft_eye_left_y(Integer.valueOf(contents[9]));
					fif.setLeft_eye_right_x(Integer.valueOf(contents[10]));
					fif.setLeft_eye_right_y(Integer.valueOf(contents[11]));
					fif.setRight_eye_left_x(Integer.valueOf(contents[12]));
					fif.setRight_eye_left_y(Integer.valueOf(contents[13]));
					fif.setRight_eye_right_x(Integer.valueOf(contents[14]));
					fif.setRight_eye_right_y(Integer.valueOf(contents[15]));
					fif.setMouth_left_x(Integer.valueOf(contents[16]));
					fif.setMouth_left_y(Integer.valueOf(contents[17]));
					fif.setMouth_right_x(Integer.valueOf(contents[18]));
					fif.setMouth_right_y(Integer.valueOf(contents[19]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return filist;
		}
}
