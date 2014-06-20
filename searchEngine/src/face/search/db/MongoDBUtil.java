package face.search.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.examples.BaileyBorweinPlouffe;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import config.ConfigConstant;
import face.search.bean.Photo;
import face.search.util.SimilarImageSearch;


public final class MongoDBUtil {  

    private static Mongo mongo;  
  
    private static DB db;  
    
    private static DBCollection faceLabels;
    
    static {  
        try {  
            mongo = new Mongo();  
            db = mongo.getDB(ConfigConstant.DB_NAME);  
            faceLabels = db.getCollection(ConfigConstant.TABLE_NAME);
            // db.authenticate(username, passwd)  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (MongoException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private MongoDBUtil() {}              
      
    public static boolean collectionExists(String collectionName) {  
        return db.collectionExists(collectionName);  
    }
    
    public static void dropCollection(String collectionName) {
    	getCollection(collectionName).drop();
	}
    
    public static void mergeUpdate(String pid, String attribute, String Value) {
    	BasicDBObject query = new BasicDBObject("pid", pid);
    	DBCursor cursor = faceLabels.find(query);
    	DBObject record = null;
    	if (cursor.hasNext()) {
    		record = cursor.next();
    		record.put(attribute, Value);
    	} else {
    		record = new BasicDBObject("pid", pid).append(attribute, Value);
    	}
    	faceLabels.update(query, record, true, false);
    }
    
    public static DBCollection getCollection(String collectionName) {  
        return db.getCollection(collectionName);  
    }
    
    public static List<Photo> findAllMatchedResult(String params) {
    	Map<String, String> paramMap = splitParams(params);
    	List<Photo> results = new ArrayList<Photo>();
    	if (paramMap.keySet().size() == 0) {
    		return results;
    	}
    	
    	BasicDBObject query = new BasicDBObject();
    	for (String key: paramMap.keySet()) {
    		query.append(key, paramMap.get(key));
    	}
    	List<DBObject> dbRes = faceLabels.find(query).toArray();
    	for (DBObject dbo : dbRes) {
    		Photo photo = new Photo();
    		String pid = dbo.get("pid").toString();
    		photo.setName(pid+ConfigConstant.PicSuffix);
    		photo.setPath(photo.getName());
    		photo.setDescirption(photo.getName());
    		results.add(photo);
    	}
    	return results;
    }
    
    public static List<String> findAllPids() {
    	List<String> pids = new ArrayList<String>();
    	DBCursor cur = faceLabels.find();
    	while (cur.hasNext()) {
    		pids.add(cur.next().get("pid").toString());
    	}
    	return pids;
    }
    
    public static List<Photo> findTop3SimilarPhoto(String sourceFingerprint) {
    	List<Photo> temp = new LinkedList<Photo>();
    	List<DBObject> dbRes = faceLabels.find().toArray();
    	for (DBObject dbo : dbRes) {
    		Photo photo = new Photo();
    		String pid = dbo.get("pid").toString();
    		String fingerprint = dbo.get("fingerprint").toString();
    		int hammingDistance = SimilarImageSearch.hammingDistance(sourceFingerprint, fingerprint);
    		Double similarity = (16.0 - hammingDistance) / 16.0;
    		photo.setName(pid+ConfigConstant.PicSuffix);
    		photo.setPath(photo.getName());
    		photo.setDescirption(photo.getName());
    		photo.setSimilarity(similarity);
    		temp.add(photo);
    	}
    	Collections.sort(temp);
    	List<Photo> results = new LinkedList<Photo>();
    	for (int i = 0; i < 3; i ++) {
    		results.add(temp.get(i));
    	}
    	return results;
    }
    
    private static Map<String, String> splitParams(String param) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	String[] params = param.split(" ");
    	DBCollection dictionary = getCollection("dictionary");
    	for (String para : params) {
//    		Pattern pattern = Pattern.compile("^" + para+ ".*$", Pattern.CASE_INSENSITIVE);
    		DBCursor cur = dictionary.find(new BasicDBObject("value", para.toLowerCase()), new BasicDBObject("key", true));
    		while (cur.hasNext()) {
    			DBObject res = cur.next();
    			if (res.get("key")!=null) {
    				String[] value = res.get("key").toString().split(":");
    				paramMap.put(value[0], value[1]);
    			}
    		}
    	}
    	return paramMap;
    }
    
    private static void insertDicData() {
    	DBCollection dictionary = getCollection("dictionary");
    	dictionary.drop();
    	List<DBObject> dbos = new ArrayList<DBObject>();
    	DBObject dbo = new BasicDBObject("key", "gender:male").append("value", "man");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "men");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "male");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "boy");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "boys");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "menfolk");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:male").append("value", "gentleman");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "lady");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "ladies");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "miss");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "madam");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "ma'am");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "woman");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "women");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "girl");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "girls");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "fille");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "colleen");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "lassie");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "gender:female").append("value", "female");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "hair_color:blond").append("value", "blond");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "hair_color:blond").append("value", "blonde");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "hair_color:blond").append("value", "gold");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "hair_color:blond").append("value", "light-hair");
    	dbos.add(dbo);
    	dictionary.insert(dbos);
    }
    
    private static void deleteRecord() {
    	BasicDBObject dbo = new BasicDBObject("pid", "8484");
    	faceLabels.findAndRemove(dbo);
    }
    
    public static void main(String[] args) {
//		System.out.println(collectionExists("faceLabels"));
//		DBObject dbo = faceLabels.findOne();
//		System.out.println(dbo);
//    	insertDicData();
//    	findAllMatchedResult("male+blonde");
//    	findTop3SimilarPhoto("ff8c0080f0f8ffff");
//    	splitParams("woman");
    	deleteRecord();
	}
}
