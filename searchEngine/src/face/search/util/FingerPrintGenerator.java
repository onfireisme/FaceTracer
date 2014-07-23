package face.search.util;

import java.io.IOException;
import java.io.InputStreamReader;

public class FingerPrintGenerator {
	public static final String path = "E:/WorkSpaces/faceData/testPic/";
	
	public static void main(String[] args) {
//		List<String> pids = MongoDBUtil.findAllPids();
//		for (String pid : pids) {
//			String imgPath = path + pid + ConfigConstant.PicSuffix;
//			String fingerprint = SimilarImageSearch.produceFingerPrint(imgPath);
//			System.out.println(imgPath+" : "+fingerprint);
//			MongoDBUtil.mergeUpdate(pid, "fingerprint", fingerprint);
//		}
		try {
		Process p = Runtime.getRuntime().exec("cmd.exe /c E:/WorkSpaces/searchEngine/facedetect/minimal.exe");
		  
		InputStreamReader input =new InputStreamReader(p.getInputStream(),"GBK");
		char[] buf = new char[1024];
		int size;
		StringBuilder sb = new StringBuilder();
		while((size = input.read(buf)) != -1) {
		    sb.append(buf,0,size);
		}
		if ("success!".equals(sb.toString())) {
			System.out.println(sb.toString());
		}
		input.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
