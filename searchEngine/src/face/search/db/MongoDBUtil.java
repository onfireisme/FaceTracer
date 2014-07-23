package face.search.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import config.ConfigConstant;
import face.search.bean.Photo;
import face.search.bean.PhotoEvalInfo;

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
    
    public static List<Photo> findSimilarPhoto(Map<String, String> predictRes) {
    	List<Photo> results = new LinkedList<Photo>();
    	DBObject query = new BasicDBObject();
    	
    	query.putAll(predictRes);
    	List<DBObject> dbRes = faceLabels.find(query).toArray();
    	if (dbRes.size()==0) {
    		predictRes.remove("mustache");
        	predictRes.remove("blurry");
        	predictRes.remove("lighting");
        	predictRes.remove("smiling");
        	predictRes.remove("hair_color");
        	query = new BasicDBObject();
        	query.putAll(predictRes);
        	dbRes = faceLabels.find(query).toArray();
    	}
    	
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
    
    public static List<PhotoEvalInfo> getEvaluatedPhotos(){
    	List<DBObject> dbRes = faceLabels.find().toArray();
    	return wrapPhotoEvalInfos(dbRes, null);
    }
    
    private static List<PhotoEvalInfo> wrapPhotoEvalInfos(List<DBObject> dbRes, String error) {
    	List<PhotoEvalInfo> result = new LinkedList<PhotoEvalInfo>();
    	for (DBObject dbo : dbRes) {
    		PhotoEvalInfo pei = new PhotoEvalInfo();
    		String pid = dbo.get("pid").toString();
    		pei.setName(pid);
    		pei.setPath(pid+ConfigConstant.PicSuffix);
    		pei.setAge(dbo.get("age").toString());
    		pei.setBlurry(dbo.get("blurry").toString());
    		pei.setEnvironment(dbo.get("environment").toString());
    		pei.setEye_wear( dbo.get("eye_wear").toString());
    		pei.setGender( dbo.get("gender").toString());
    		pei.setHair_color( dbo.get("hair_color").toString());
    		pei.setLighting( dbo.get("lighting").toString());
    		pei.setMustache( dbo.get("mustache").toString());
    		pei.setRace( dbo.get("race").toString());
    		pei.setSmiling(dbo.get("smiling").toString());
    		if (error!=null) {
    			pei.setError(error);
    		}
    		result.add(pei);
    	}
    	return result;
    }
    
    public static int updatePhoto(PhotoEvalInfo photoEvalInfo) {
    	DBObject query = new BasicDBObject();
    	query.put("pid", photoEvalInfo.getName());
    	
    	DBObject value = new BasicDBObject();
    	value.put("age", photoEvalInfo.getAge());
    	value.put("blurry", photoEvalInfo.getBlurry());
    	value.put("environment", photoEvalInfo.getEnvironment());
    	value.put("eye_wear", photoEvalInfo.getEye_wear());
    	value.put("gender", photoEvalInfo.getGender());
    	value.put("hair_color", photoEvalInfo.getHair_color());
    	value.put("lighting", photoEvalInfo.getLighting());
    	value.put("mustache", photoEvalInfo.getMustache());
    	value.put("race", photoEvalInfo.getRace());
    	value.put("smiling", photoEvalInfo.getSmiling());
    	
    	value.put("pid", photoEvalInfo.getName());
    	if (faceLabels.findOne(value)!=null){
    		return 0;
    	}
    	
    	value.removeField("pid");
    	DBObject upsertValue=new BasicDBObject("$set", value);
    	
    	WriteResult wr = faceLabels.update(query, upsertValue, false, false);
    	return wr.getN();
    }
    
    public static List<PhotoEvalInfo> getMissPrediction() {
    	DBCollection rule = getCollection("rule");
    	List<DBObject> ruleRes = rule.find().toArray();
    	
    	List<PhotoEvalInfo> result = new LinkedList<PhotoEvalInfo>();
    	
    	for (DBObject dbo : ruleRes) {
    		List<DBObject> factors = (List<DBObject>) dbo.get("factor");
    		List<DBObject> derivations = (List<DBObject>) dbo.get("derivation");
    		
    		System.out.println("factor: "+ factors.size()+" derivation:"+derivations.size());
    		BasicDBObject query = new BasicDBObject(); 
    		
    		DBObject factor = factors.get(0);
    		DBObject derivation = derivations.get(0);
    		StringBuilder error = new StringBuilder().append("Violate ");
    		for (String key: factor.keySet()) {
    			query.append(key, factor.get(key));
    			error.append(key + ":" + factor.get(key)+" ");
    		}
    		error.append("=> ");
    		for (String key: derivation.keySet()) {
    			BasicDBObject subQuery = new BasicDBObject("$ne", derivation.get(key));
    			query.append(key, subQuery);
    			error.append(key + ":" + derivation.get(key)+" ");
    		}
    		List<DBObject> dbRes = faceLabels.find(query).toArray();
    		result.addAll(wrapPhotoEvalInfos(dbRes, error.toString()));
    	}
    	return result;
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
    
    private static void insertRuleData() {
    	DBCollection rule = getCollection("rule");
    	rule.drop();
    	List<DBObject> factor = new ArrayList<DBObject>();
    	List<DBObject> derivation = new ArrayList<DBObject>();
    	factor.add(BasicDBObjectBuilder.start().add("gender", "female").get());
    	derivation.add(BasicDBObjectBuilder.start().add("mustache", "false").get());
    	DBObject ruleObj = new BasicDBObject();
    	ruleObj.put("factor", factor);
    	ruleObj.put("derivation", derivation);
    	rule.insert(ruleObj);
    	
    	factor = new ArrayList<DBObject>();
    	derivation = new ArrayList<DBObject>();
    	factor.add(BasicDBObjectBuilder.start().add("age", "child").get());
    	derivation.add(BasicDBObjectBuilder.start().add("mustache", "false").get());
    	ruleObj = new BasicDBObject();
    	ruleObj.put("factor", factor);
    	ruleObj.put("derivation", derivation);
    	rule.insert(ruleObj);
    	
    	factor = new ArrayList<DBObject>();
    	derivation = new ArrayList<DBObject>();
    	factor.add(BasicDBObjectBuilder.start().add("age", "baby").get());
    	derivation.add(BasicDBObjectBuilder.start().add("mustache", "false").get());
    	ruleObj = new BasicDBObject();
    	ruleObj.put("factor", factor);
    	ruleObj.put("derivation", derivation);
    	rule.insert(ruleObj);
    	
    	factor = new ArrayList<DBObject>();
    	derivation = new ArrayList<DBObject>();
    	factor.add(BasicDBObjectBuilder.start().add("smiling","true").add("lighting","flash").get());
    	derivation.add(BasicDBObjectBuilder.start().add("environment", "indoor").get());
    	ruleObj = new BasicDBObject();
    	ruleObj.put("factor", factor);
    	ruleObj.put("derivation", derivation);
    	rule.insert(ruleObj);
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
    	dbo = new BasicDBObject("key", "age:baby").append("value", "baby");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "child");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "children");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:youth").append("value", "youth");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:youth").append("value", "young");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "middle_aged");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "senior");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "old");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:asian").append("value", "asian");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:asian").append("value", "asia");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:white").append("value", "white");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:white").append("value", "caucasian");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:black").append("value", "black");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:black").append("value", "blacks");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "race:black").append("value", "negro");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:none").append("value", "none");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:eyeglasses").append("value", "eyeglasses");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:eyeglasses").append("value", "eyeglass");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:eyeglasses").append("value", "glass");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:eyeglasses").append("value", "glasses");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:sunglasses").append("value", "sunglasses");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:sunglasses").append("value", "sunglass");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:sunglasses").append("value", "glass");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "eye_wear:sunglasses").append("value", "glasses");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "boy");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "boys");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "girl");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:child").append("value", "girls");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "lady");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "ladies");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:youth").append("value", "miss");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:youth").append("value", "madam");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:youth").append("value", "ma'am");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "miss");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "madam");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "ma'am");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "woman");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:middle_aged").append("value", "women");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "ladies");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "miss");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "madam");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "ma'am");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "woman");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "age:senior").append("value", "women");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "mustache:true").append("value", "mustache");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "blurry:true").append("value", "blurry");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "blurry:true").append("value", "blur");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "blurry:true").append("value", "blurred");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "lighting:harsh").append("value", "harsh");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "lighting:flash").append("value", "flash");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "environment:outdoor").append("value", "outdoor");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "environment:outdoor").append("value", "outside");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "environment:indoor").append("value", "indoor");
    	dbos.add(dbo);
    	dbo = new BasicDBObject("key", "environment:indoor").append("value", "inside");
    	dbos.add(dbo);
    	dictionary.insert(dbos);
    }
    
    private static void deleteRecord() {
    	BasicDBObject dbo = new BasicDBObject("pid", "@attribute faceId string");
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
//    	deleteRecord();
//    	getEvaluatedPhotos();
//    	List<PhotoEvalInfo> plist = getEvaluatedPhotos();
//    	PhotoEvalInfo p = plist.get(0);
//    	p.setBlurry("true");
//    	System.out.println(updatePhoto(p));
//    	List<PhotoEvalInfo> abc = getEvaluatedPhotos();
//    	PhotoEvalInfo p = abc.get(0);
//    	p.setGender("female");
//    	System.out.println(updatePhoto(p));
//    	System.out.println("pid: "+p.getName());
//    	insertRuleData();
    	getMissPrediction();
	}
}
