package face.feature.db;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBConnector;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

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
    
    public static DBCollection getCollection(String collectionName) {  
        return db.getCollection(collectionName);  
    }
    public static void main(String[] args) {
		System.out.println(collectionExists("faceLabels"));
		DBObject dbo = faceLabels.findOne();
		System.out.println(dbo);
	}
}
