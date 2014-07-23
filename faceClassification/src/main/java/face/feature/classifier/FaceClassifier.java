package face.feature.classifier;

import java.io.Serializable;
import java.util.List;

public class FaceClassifier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<double[]> weightsList = null;
	private List<Double> biasList = null;
	private List<Double> Bts = null;
	private List<Integer> classifierIds = null;
	
	public FaceClassifier(){}
	
	public FaceClassifier(List<double[]> weightsList, List<Double> biasList,
			List<Double> bts, List<Integer> classifierIds) {
		super();
		this.weightsList = weightsList;
		this.biasList = biasList;
		Bts = bts;
		this.classifierIds = classifierIds;
	}
	public List<double[]> getWeightsList() {
		return weightsList;
	}
	public void setWeightsList(List<double[]> weightsList) {
		this.weightsList = weightsList;
	}
	public List<Double> getBiasList() {
		return biasList;
	}
	public void setBiasList(List<Double> biasList) {
		this.biasList = biasList;
	}
	public List<Double> getBts() {
		return Bts;
	}
	public void setBts(List<Double> bts) {
		Bts = bts;
	}
	public List<Integer> getClassifierIds() {
		return classifierIds;
	}
	public void setClassifierIds(List<Integer> classifierIds) {
		this.classifierIds = classifierIds;
	}
	
	
}
