package face.feature.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBConnector;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import face.feature.bean.PhotoEvaInfo;
import face.feature.extraction.ConfigConstant;

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
    
    public static List<PhotoEvaInfo> getEvaResult() {
    	List<PhotoEvaInfo> plist = new ArrayList<PhotoEvaInfo>();
    	List<DBObject> dbRes = faceLabels.find().toArray();
    	for (DBObject dbo : dbRes) {
    		PhotoEvaInfo pei = new PhotoEvaInfo();
    		Map<String, String> result = new HashMap<String, String>();
    		pei.setPid(dbo.get("pid").toString());
    		result.put("age", dbo.get("age").toString());
    		result.put("blurry", dbo.get("blurry").toString());
    		result.put("environment", dbo.get("environment").toString());
    		result.put("eye_wear", dbo.get("eye_wear").toString());
    		result.put("gender", dbo.get("gender").toString());
    		result.put("hair_color", dbo.get("hair_color").toString());
    		result.put("lighting", dbo.get("lighting").toString());
    		result.put("mustache", dbo.get("mustache").toString());
    		result.put("race", dbo.get("race").toString());
    		result.put("smiling", dbo.get("smiling").toString());
    		pei.setResult(result);
    		plist.add(pei);
    	}
    	return plist;
    }
    
    public static DBCollection getCollection(String collectionName) {  
        return db.getCollection(collectionName);  
    }
    public static void main(String[] args) {
		System.out.println(collectionExists("faceLabels"));
		DBObject dbo = faceLabels.findOne();
		System.out.println(dbo);
	}
}
