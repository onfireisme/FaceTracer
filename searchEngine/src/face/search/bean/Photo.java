package face.search.bean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Photo implements Comparable<Photo>{
	private String name;
	private String descirption;
	private String path;
	private Double similarity;
	
	@Override
	public int compareTo(Photo o) {
		return o.getSimilarity().compareTo(this.getSimilarity());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDescirption() {
		return descirption;
	}
	public void setDescirption(String descirption) {
		this.descirption = descirption;
	}
	public Double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}
}
