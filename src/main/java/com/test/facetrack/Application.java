package com.test.facetrack;

import com.test.facetrack.engine.FaceTrackingdEngine;
import com.test.facetrack.window.CameraFace;

public class Application {
	/**
	 * 启动人脸跟踪系统
	 * @param args
	 */
	public static void main(String[] args) {
		// 人脸追踪引擎初始化
		FaceTrackingdEngine faceTrackingEngine = new FaceTrackingdEngine();
		// 摄像头初始化
		CameraFace cameraFace = new CameraFace();
		// 启动摄像头
		cameraFace.Start(faceTrackingEngine);;
	}

}
