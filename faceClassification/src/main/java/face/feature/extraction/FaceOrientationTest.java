package face.feature.extraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FaceOrientationTest {
  public static int getForntalPose () {
	  int id = 0;
	  String s = null;
	  int count = 0;
	  try {
		FileInputStream fis = new FileInputStream(ConfigConstant.path+ConfigConstant.faceStatsName);
			InputStreamReader ir = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(ir);
			while((s=br.readLine())!=null)
	        {
				if (s.startsWith("#")) {
	        		continue;
				}
				String[] contents = s.split("\t");
				if (contents[5].equals("0")&&contents[6].equals("0")&&contents[7].equals("0")) {
					id = Integer.valueOf(contents[0]);
					count ++;
					if (count >3) {
						break;
					}
				}
				
	        }
	} catch (Exception e) {
		e.printStackTrace();
	}
	return id;
  }
  
  public static void main(String[] args) {
	System.out.println(getForntalPose());
  }
}
