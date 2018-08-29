package com.test.facematch;

import java.util.ArrayList;
import java.util.List;
import com.test.facematch.engine.FaceDetectedEngine;
import com.test.facematch.engine.FaceMatchingEngine;
import com.test.facematch.entity.ASVLOFFSCREEN;
import com.test.facematch.entity.User;
import com.test.facematch.utils.ImageLoader;
import com.test.facematch.window.CameraFace;

public class Application {
	public static void main(String[] args) {
		// 人脸检测引擎初始化
		FaceDetectedEngine fDEngine = new FaceDetectedEngine();
		// 人脸比对引擎初始化
		FaceMatchingEngine fREngine = new FaceMatchingEngine();
		// 读取能被识别人脸图像
		String img1 = "./images/test1.jpg";
		String img2 = "./images/test2.jpg";
		String img3 = "./images/test3.jpg";
		ASVLOFFSCREEN asv1 = ImageLoader.getImageFromFile(img1);
		ASVLOFFSCREEN asv2 = ImageLoader.getImageFromFile(img2);
		ASVLOFFSCREEN asv3 = ImageLoader.getImageFromFile(img3);
		// 创建可以被识别的用户
		User user1 = new User("权志龙","001",asv1,img1);
		User user2 = new User("成龙" ,"002",asv2,img2);
		User user3 = new User("小豆丁" ,"003",asv3,img3);
		// 将用户加入集合
		List<User> userList = new ArrayList<User>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		// 获取面部位置集合
		List<User> userFaceInfoList = fDEngine.doFaceDetectionAll(userList);
		// 获取特征值集合
		List<User> userFeatureList = fREngine.extractFRFeatureAll(userFaceInfoList);
		// 开始摄像头
		CameraFace cameraFace = new CameraFace();
		// 设置启动前的参数
		cameraFace.setUserData(userFeatureList);
		cameraFace.setFDEngine(fDEngine);
		cameraFace.setFREngine(fREngine);
		// 启动摄像头
		cameraFace.Start();
	}
	
}
