package com.test.facetrack.entity;
/**
 * 人脸检测的位置
 */
public class FaceInfo {
    public int left;
    public int top;
    public int right;
    public int bottom;
    public int orient;
    
	public FaceInfo() {}
	public FaceInfo(int left, int top, int right, int bottom, int orient) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.orient = orient;
	}
	
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getOrient() {
		return orient;
	}
	public void setOrient(int orient) {
		this.orient = orient;
	}

	@Override
	public String toString() {
		return "FaceInfo [left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + ", orient="
				+ orient + "]";
	}
    
}
