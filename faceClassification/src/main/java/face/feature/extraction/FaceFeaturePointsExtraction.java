package face.feature.extraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import face.feature.bean.FaceIndex;

public class FaceFeaturePointsExtraction {
	private static List<FaceIndex> filist = null;
	
	public static List<FaceIndex> getFaceDataInstance() {
		if (filist == null) {
			filist = getFacePoints();
		}
		return filist;
	}
	
	public static List<FaceIndex> getFacePoints() {
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		Set<String> faceIds = new HashSet<String>();
		String s = null;
		int count = 0;
		try {
			FileInputStream fis = new FileInputStream(ConfigConstant.path+ConfigConstant.faceLandMarks);
			InputStreamReader ir = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(ir);
			
			while ((s= br.readLine()) != null) {
				count++;
				String[] contents = s.split("  ");
				int dotIndex = contents[0].lastIndexOf(".");
				String faceID = contents[0].substring(0, dotIndex);
				if (faceIds.contains(faceID)) {
					faceID += "_" + contents[1];
				} else {
					faceIds.add(faceID);
				}
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
				filist.add(fif);
			}
		} catch (Exception e) {
			System.out.println(s);
			System.out.println("line count:"+count);
			e.printStackTrace();
		}
		return filist;
	}
}
