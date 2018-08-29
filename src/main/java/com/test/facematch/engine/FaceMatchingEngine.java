package com.test.facematch.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;
import com.test.facematch.entity.ASVLOFFSCREEN;
import com.test.facematch.entity.FaceInfo;
import com.test.facematch.entity.FaceInput;
import com.test.facematch.entity.FaceModel;
import com.test.facematch.entity.User;
import com.test.facematch.entity.FRSdkVersion;
import com.test.facematch.libs.CLibrary;
import com.test.facematch.libs.FaceMatchingLibrary;
import com.test.facematch.utils.ImageLoader;
/**
 * 人脸比对引擎
 */
public class FaceMatchingEngine {
	// AppId设置
	private String appid;
	// 人脸匹配SDK-key
	private String frSdkKey;
	// 分配给引擎使用的内存大小
	public static final int FR_WORKBUF_SIZE = 40 * 1024 * 1024;
	// 用户期望引擎最多能检测出的人脸数 有效值范围[1,50]
	public static final int MAX_FACE_NUM = 10;
	// 用于数值表示的最小人脸尺寸 有效值范围[2,50] 推荐值 16。该尺寸是人脸相对于所在图片的长边的占比。
	// 例如，如果用户想检测到的最小人脸尺寸是图片长度的1/8，那么这个nScale就应该设置为8
	public static final int MIN_FACE_RECOGNITION_RE_SIZE = 20;
	// 人脸比对引擎
	private Pointer FREngine;
	// 分配给引擎使用的内存地址
	private Pointer FRWorkMem;
	// 人脸比对依赖
	private FaceMatchingLibrary FRLibrary;
	
	/**
	 * 构造方法,执行初始化
	 */
	public FaceMatchingEngine(){
		// 初始化参数设置
		init();
		// 初始化人脸匹配引擎
		initFaceMatching();
	}
	
	/**
	 * 初始化参数
	 */
	private void init() {
		try {
			// 分配给引擎使用的内存地址
			FRWorkMem = CLibrary.malloc(FR_WORKBUF_SIZE);
			// 创建Properties
			Properties properties = new Properties();
			// 加载properties文件
			InputStream is = FaceMatchingEngine.class.getClassLoader().getResourceAsStream("application.properties");
			// 加载输入流
			properties.load(is);
			// 设置key和FD-SDKKEY
			appid = properties.getProperty("appid");
			frSdkKey = properties.getProperty("fr_sdkkey");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化人脸比对引擎
	 */
	private void initFaceMatching(){
		// 载入人脸比对依赖库
		FRLibrary = new FaceMatchingLibrary();
		// 初始化人匹配别引擎
		PointerByReference faceDetectionEngine = new PointerByReference();
		// 初始化人脸匹配引擎
		NativeLong ret = FRLibrary.initFaceMatchingEngine(appid, frSdkKey,FRWorkMem,FR_WORKBUF_SIZE,faceDetectionEngine);
		// 判断人脸匹配引擎是否启动成功，失败则直接清理内存，退出系统
		if (ret.longValue() != 0) {
			// 释放内存
			CLibrary.free(FRWorkMem);
			System.out.println(String.format("face matching init fail! ERROR--:0x%x", ret.longValue()));
			// 关闭系统
			System.exit(0);
		}
		// 获取引擎值
		FREngine = faceDetectionEngine.getValue();
		// 获取版本
		FRSdkVersion version = FRLibrary.getVersion(FREngine);
		System.out.println(version.getVersion());
	}
	
	/**
	 * 人脸特征值提取
	 * @param hFREngine 引擎handle
	 * @param inputImg 待检测图像
	 * @param faceInfo 通过人脸检测或人脸追踪检测出的人脸信息
	 * @return
	 */
	public FaceModel extractFRFeature(ASVLOFFSCREEN inputImg, FaceInfo faceInfo) {
		// 将通过人脸检测或人脸追踪检测出的人脸信息存入FaceModel
		FaceInput faceinput = new FaceInput();
		faceinput.lOrient = faceInfo.orient;
		faceinput.rcFace.left = faceInfo.left;
		faceinput.rcFace.top = faceInfo.top;
		faceinput.rcFace.right = faceInfo.right;
		faceinput.rcFace.bottom = faceInfo.bottom;
		// 用于存储特征值对象faceFeature
		FaceModel faceFeature = new FaceModel();
		// 获取特征值
		NativeLong ret = FRLibrary.faceExtractFRFeature(FREngine, inputImg, faceinput,faceFeature);
		// 如果获取特征值失败则返回null
		if (ret.longValue() != 0) {
			System.out.println(String.format("extract face feature fail! ERROR--: 0x%x", ret.longValue()));
			return null;
		}
		// 返回特征值
		try {
			return faceFeature.deepCopy();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 分析全部特征值
	 * @param list 待分析人脸位置集合
	 * @return 特征值集合
	 */
	public List<User> extractFRFeatureAll(List<User> list){
		// 创建返回的集合
		List<User> userList = new ArrayList<User>();
		// 循环分析全部图像
		for(User user:list) {
			// 分析特征值
			FaceModel faceModel = extractFRFeature(user.getAsv(),user.getFaceInfo());
			// 将特征值添加进用户
			user.setFaceModel(faceModel);
			// 将分析完的特征值加入集合
			userList.add(user);
		}
		// 返回特征值集合
		return userList;
	}

	/**
	 * 人脸特征值比较
	 * @param hFREngine 人脸匹配引擎
	 * @param faceFeatureA 特征值A
	 * @param faceFeatureB 特征值B
	 * @return
	 */
	public float compareFaceSimilarity(FaceModel faceFeatureA, FaceModel faceFeatureB) {
		// 验证不为空
		if (faceFeatureA == null) {
			System.out.println("faceFeatureA is null");
			return 0.0f;
		}
		if (faceFeatureB == null) {
			System.out.println("faceFeatureB is null");
			return 0.0f;
		}
		// 设置存储相似度分数的变量
		FloatByReference faceScore = new FloatByReference(0.0f);
		// 脸部特征比较
		NativeLong ret = FRLibrary.facePairMatchinge(FREngine, faceFeatureA, faceFeatureB,faceScore);
		// 释放特征值A和B的内存空间
		//faceFeatureA.freeUnmanaged();
		//faceFeatureB.freeUnmanaged();
		// 验证是否执行成功
		if (ret.longValue() != 0) {
			System.out.println(String.format("Failure of face matching!ERROR--:0x%x", ret.longValue()));
			return 0.0f;
		}
		// 返回相似度分数
		return faceScore.getValue();
	}
	
	/**
	 * 获得人脸检测引擎
	 * @return 引擎
	 */
	public Pointer getFDEngine(){
		return FREngine;
	}
		
	
	/**
	 * 销毁引擎,释放相应资源
	 */
	public void uninitialFaceEngine() {
		if(FREngine!=null){
			// 销毁引擎
			FRLibrary.uninitialFaceEngine(FREngine);
			// 释放占用的内存
			CLibrary.free(FRWorkMem);
		}
	}
	
	/**
	 * 测试启动引擎
	 * @param args
	 */
	public static void main(String[] args) {
		String img1 = "./images/test1.jpg";
		String img2 = "./images/test3.jpg";
		ASVLOFFSCREEN asv1 = ImageLoader.getImageFromFile(img1);
		ASVLOFFSCREEN asv2 = ImageLoader.getImageFromFile(img2);
		FaceDetectedEngine fde = new FaceDetectedEngine();
		FaceInfo[] face1 = fde.doFaceDetection(asv1);
		FaceInfo[] face2 = fde.doFaceDetection(asv2);
		FaceMatchingEngine fme = new FaceMatchingEngine();
		FaceModel faceModel1 = fme.extractFRFeature(asv1, face1[0]);
		FaceModel faceModel2 = fme.extractFRFeature(asv2, face2[0]);
		float score =fme.compareFaceSimilarity(faceModel1,faceModel2);
		
		System.out.println(faceModel1);
		System.out.println(faceModel2);
		System.out.println(score);
	}
}
