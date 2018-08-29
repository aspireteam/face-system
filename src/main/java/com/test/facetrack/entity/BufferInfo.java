package com.test.facetrack.entity;

import java.util.Arrays;
/**
 * 用于存储读取进的照片
 */
public class BufferInfo {
    public int width;
    public int height;
    public byte[] buffer;
    
    public BufferInfo() {}
	public BufferInfo(int width, int height, byte[] buffer) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public byte[] getBuffer() {
		return buffer;
	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public String toString() {
		return "BufferInfo [width=" + width + ", height=" + height + ", buffer=" + Arrays.toString(buffer) + "]";
	}
	
}