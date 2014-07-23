package face.feature.bean;

public class FaceIndex {
	String id;
	String url;
	String attribute;
	String category;
	String label;
	int crop_w;
	int crop_h;
	int crop_x0;
	int crop_y0;
	int yaw;
	int pitch;
	int roll;
	int left_eye_left_x;
	int left_eye_left_y;
	int left_eye_right_x;
	int left_eye_right_y;
	int right_eye_left_x;
	int right_eye_left_y;
	int right_eye_right_x;
	int right_eye_right_y;
	int mouth_left_x;
	int mouth_left_y;
	int mouth_right_x;
	int mouth_right_y;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getCrop_w() {
		return crop_w;
	}
	public void setCrop_w(int crop_w) {
		this.crop_w = crop_w;
	}
	public int getCrop_h() {
		return crop_h;
	}
	public void setCrop_h(int crop_h) {
		this.crop_h = crop_h;
	}
	public int getCrop_x0() {
		return crop_x0;
	}
	public void setCrop_x0(int crop_x0) {
		this.crop_x0 = crop_x0;
	}
	public int getCrop_y0() {
		return crop_y0;
	}
	public void setCrop_y0(int crop_y0) {
		this.crop_y0 = crop_y0;
	}
	public int getYaw() {
		return yaw;
	}
	public void setYaw(int yaw) {
		this.yaw = yaw;
	}
	public int getPitch() {
		return pitch;
	}
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	public int getRoll() {
		return roll;
	}
	public void setRoll(int roll) {
		this.roll = roll;
	}
	public int getLeft_eye_left_x() {
		return left_eye_left_x;
	}
	public void setLeft_eye_left_x(int left_eye_left_x) {
		this.left_eye_left_x = left_eye_left_x;
	}
	public int getLeft_eye_left_y() {
		return left_eye_left_y;
	}
	public void setLeft_eye_left_y(int left_eye_left_y) {
		this.left_eye_left_y = left_eye_left_y;
	}
	public int getLeft_eye_right_x() {
		return left_eye_right_x;
	}
	public void setLeft_eye_right_x(int left_eye_right_x) {
		this.left_eye_right_x = left_eye_right_x;
	}
	public int getLeft_eye_right_y() {
		return left_eye_right_y;
	}
	public void setLeft_eye_right_y(int left_eye_right_y) {
		this.left_eye_right_y = left_eye_right_y;
	}
	public int getRight_eye_left_x() {
		return right_eye_left_x;
	}
	public void setRight_eye_left_x(int right_eye_left_x) {
		this.right_eye_left_x = right_eye_left_x;
	}
	public int getRight_eye_left_y() {
		return right_eye_left_y;
	}
	public void setRight_eye_left_y(int right_eye_left_y) {
		this.right_eye_left_y = right_eye_left_y;
	}
	public int getRight_eye_right_x() {
		return right_eye_right_x;
	}
	public void setRight_eye_right_x(int right_eye_right_x) {
		this.right_eye_right_x = right_eye_right_x;
	}
	public int getRight_eye_right_y() {
		return right_eye_right_y;
	}
	public void setRight_eye_right_y(int right_eye_right_y) {
		this.right_eye_right_y = right_eye_right_y;
	}
	public int getMouth_left_x() {
		return mouth_left_x;
	}
	public void setMouth_left_x(int mouth_left_x) {
		this.mouth_left_x = mouth_left_x;
	}
	public int getMouth_left_y() {
		return mouth_left_y;
	}
	public void setMouth_left_y(int mouth_left_y) {
		this.mouth_left_y = mouth_left_y;
	}
	public int getMouth_right_x() {
		return mouth_right_x;
	}
	public void setMouth_right_x(int mouth_right_x) {
		this.mouth_right_x = mouth_right_x;
	}
	public int getMouth_right_y() {
		return mouth_right_y;
	}
	public void setMouth_right_y(int mouth_right_y) {
		this.mouth_right_y = mouth_right_y;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FaceIndex other = (FaceIndex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
