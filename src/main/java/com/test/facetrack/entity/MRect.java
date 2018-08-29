package com.test.facetrack.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
/**
 * 用于存储人脸坐标的实体类
 */
public class MRect extends Structure {

	public int left;
	public int top;
	public int right;
	public int bottom;

	public MRect() {}

	public MRect(Pointer p) {
		super(p);
		read();
	}

	public static class ByValue extends MRect implements Structure.ByValue {
		public ByValue() {}
		public ByValue(Pointer p) {
			super(p);
		}
	}

	public static class ByReference extends MRect implements Structure.ByReference {
		public ByReference() {}
		public ByReference(Pointer p) {
			super(p);
		}
	};

	@Override
	protected List<String> getFieldOrder() {
		List<String> list = new ArrayList<String>();
		list.add("left");
		list.add("top");
		list.add("right");
		list.add("bottom");
		return list;
	}

}
