package face.feature.extraction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import face.feature.bean.FaceIndex;
import face.feature.bean.PhotoEvaInfo;
import face.feature.db.MongoDBUtil;


public class FaceLabelRegenerator {
	public static DateFormat df = new SimpleDateFormat("yyyyMMdd_HH");
	public static void main(String[] args) {
		List<PhotoEvaInfo> plist = MongoDBUtil.getEvaResult();
		List<FaceIndex> flist = readSpecificData(plist);
		StringBuilder output = new StringBuilder();
		
		for (PhotoEvaInfo pei : plist) {
			for (String attribute : pei.getResult().keySet()) {
				FaceIndex fi = new FaceIndex();
				fi.setId(pei.getPid());
				fi.setAttribute(attribute);
				fi.setLabel(pei.getResult().get(attribute));
				flist.add(fi);
			}
		}
		
		for (FaceIndex fi : flist) {
			output.append(fi.getId()+"\t"+fi.getAttribute()+"\t"+fi.getLabel()+System.lineSeparator());
		}
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(new File(ConfigConstant.path+"facelabels"+df.format(new Date())+".txt")));
			pw.write(output.toString());
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.close();
		}
	}
	
	private static List<FaceIndex> readSpecificData(List<PhotoEvaInfo> plist){
		List<FaceIndex> filist = new ArrayList<FaceIndex>();
		String s = null;
		PhotoEvaInfo pei = new PhotoEvaInfo();
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
				pei.setPid(contents[0]);
				if (plist.contains(pei)){
					continue;
				}
				
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
	
}
