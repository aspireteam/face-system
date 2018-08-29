package com.test.facematch.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
/**
 * 用于存储图像信息实体
 */
public class ASVLOFFSCREEN extends Structure {
	public int u32PixelArrayFormat;
	public int i32Width;
	public int i32Height;
	public Pointer[] ppu8Plane = new Pointer[4];
	public int[] pi32Pitch = new int[4];

	public ASVLOFFSCREEN() {}
	public ASVLOFFSCREEN(int u32PixelArrayFormat, int i32Width, int i32Height, Pointer[] ppu8Plane, int[] pi32Pitch) {
		super();
		this.u32PixelArrayFormat = u32PixelArrayFormat;
		this.i32Width = i32Width;
		this.i32Height = i32Height;
		this.ppu8Plane = ppu8Plane;
		this.pi32Pitch = pi32Pitch;
	}

	@Override
	protected List<String> getFieldOrder() {
		List<String> list = new ArrayList<String>();
		list.add("u32PixelArrayFormat");
		list.add("i32Width");
		list.add("i32Height");
		list.add("ppu8Plane");
		list.add("pi32Pitch");
		return list;
	}
	
	
}
