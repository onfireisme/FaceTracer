package face.search.util;

import java.util.List;

import config.ConfigConstant;

import face.search.db.MongoDBUtil;

public class FingerPrintGenerator {
	public static final String path = "E:/WorkSpaces/faceData/testPic/";
	public static void main(String[] args) {
		List<String> pids = MongoDBUtil.findAllPids();
		for (String pid : pids) {
			String imgPath = path + pid + ConfigConstant.PicSuffix;
			String fingerprint = SimilarImageSearch.produceFingerPrint(imgPath);
			System.out.println(imgPath+" : "+fingerprint);
			MongoDBUtil.mergeUpdate(pid, "fingerprint", fingerprint);
		}
	}
}
