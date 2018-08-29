package com.test.facematch.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Structure;
/**
 * 存储人脸检测版本信息实体
 */
public class FDSdkVersion extends Structure {
	public int lCodebase;
	public int lMajor;
	public int lMinor;
	public int lBuild;
	public String Version;
	public String BuildDate;
	public String CopyRight;

	@Override
	protected List<String> getFieldOrder() {
		List<String> list = new ArrayList<String>();
		list.add("lCodebase");
		list.add("lMajor");
		list.add("lMinor");
		list.add("lBuild");
		list.add("Version");
		list.add("BuildDate");
		list.add("CopyRight");
		return list;
	}

	public int getlCodebase() {
		return lCodebase;
	}

	public void setlCodebase(int lCodebase) {
		this.lCodebase = lCodebase;
	}

	public int getlMajor() {
		return lMajor;
	}

	public void setlMajor(int lMajor) {
		this.lMajor = lMajor;
	}

	public int getlMinor() {
		return lMinor;
	}

	public void setlMinor(int lMinor) {
		this.lMinor = lMinor;
	}

	public int getlBuild() {
		return lBuild;
	}

	public void setlBuild(int lBuild) {
		this.lBuild = lBuild;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getBuildDate() {
		return BuildDate;
	}

	public void setBuildDate(String buildDate) {
		BuildDate = buildDate;
	}

	public String getCopyRight() {
		return CopyRight;
	}

	public void setCopyRight(String copyRight) {
		CopyRight = copyRight;
	}

}
