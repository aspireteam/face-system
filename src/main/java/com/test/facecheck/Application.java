package com.test.facecheck;

import com.test.facecheck.engine.FaceDetectedEngine;
import com.test.facecheck.window.CameraFace;

public class Application {
	/**
	 * 启动人脸检测系统
	 * @param args
	 */
	public static void main(String[] args) {
		// 人脸检测引擎初始化
		FaceDetectedEngine faceCheckEngine = new FaceDetectedEngine();
		// 摄像头初始化
		CameraFace cameraFace = new CameraFace();
		// 启动摄像头
		cameraFace.Start(faceCheckEngine);;
	}

}
