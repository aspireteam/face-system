package com.test.facematch.entity;
/**
 * 用于存储用户信息的实体
 */
public class User {
	private String name;
	private String num;
	private ASVLOFFSCREEN asv;
	private FaceModel faceModel;
	private FaceInfo faceInfo;
	private String image;

	public User() {
	}

	public User(String name, String num, ASVLOFFSCREEN asv,String image) {
		super();
		this.name = name;
		this.num = num;
		this.asv = asv;
		this.image = image;
	}

	public User(String name, String num, FaceModel faceModel) {
		super();
		this.name = name;
		this.num = num;
		this.faceModel = faceModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public ASVLOFFSCREEN getAsv() {
		return asv;
	}

	public void setAsv(ASVLOFFSCREEN asv) {
		this.asv = asv;
	}

	public FaceModel getFaceModel() {
		return faceModel;
	}

	public void setFaceModel(FaceModel faceModel) {
		this.faceModel = faceModel;
	}

	public FaceInfo getFaceInfo() {
		return faceInfo;
	}

	public void setFaceInfo(FaceInfo faceInfo) {
		this.faceInfo = faceInfo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", num=" + num + ", asv=" + asv + ", faceModel=" + faceModel + ", faceInfo="
				+ faceInfo + ", image=" + image + "]";
	}

}
