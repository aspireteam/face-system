package com.test.facecheck.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.test.facecheck.constant.FdOrientPriority;
import com.test.facecheck.entity.ASVLOFFSCREEN;
import com.test.facecheck.entity.MRect;
import com.test.facecheck.entity.FaceInfo;
import com.test.facecheck.entity.Faceres;
import com.test.facecheck.entity.SdkVersion;
import com.test.facecheck.libs.CLibrary;
import com.test.facecheck.libs.FaceDetectionLibrary;
/**
 * 人脸检测引擎
 */
public class FaceDetectedEngine {
	// AppId设置
	private String appid;
	// SDK-key设置
	private String fdSdkKey;
	// 分配给引擎使用的内存大小
	public static final int FD_WORKBUF_SIZE = 20 * 1024 * 1024;
	// 用户期望引擎最多能检测出的人脸数 有效值范围[1,50]
	public static final int MAX_FACE_NUM = 10;
	// 用于数值表示的最小人脸尺寸 有效值范围[2,50] 推荐值 16。该尺寸是人脸相对于所在图片的长边的占比。
	// 例如，如果用户想检测到的最小人脸尺寸是图片长度的1/8，那么这个nScale就应该设置为8
	public static final int MIN_FACE_RECOGNITION_RE_SIZE = 20;
	// 人脸检测引擎
	private Pointer FDEngine;
	// 分配给引擎使用的内存地址
	private Pointer FDWorkMem;
	// 人脸检测依赖
	private FaceDetectionLibrary FDLibrary;
	
	/**
	 * 构造方法,执行初始化
	 */
	public FaceDetectedEngine(){
		// 初始化参数设置
		init();
		// 初始化人脸检测引擎
		initFaceDetection();
	}
	
	/**
	 * 初始化参数
	 */
	private void init() {
		try {
			// 分配给引擎使用的内存地址
			FDWorkMem = CLibrary.malloc(FD_WORKBUF_SIZE);
			// 创建Properties
			Properties properties = new Properties();
			// 加载properties文件
			InputStream is = FaceDetectedEngine.class.getClassLoader().getResourceAsStream("application.properties");
			// 加载输入流
			properties.load(is);
			// 设置key和FD-SDKKEY
			appid = properties.getProperty("appid");
			fdSdkKey = properties.getProperty("fd_sdkkey");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 初始化人脸检测引擎
	 */
	private void initFaceDetection(){
		// 载入人脸检测依赖库
		FDLibrary = new FaceDetectionLibrary();
		// 初始化人脸识别引擎
		PointerByReference faceDetectionEngine = new PointerByReference();
		// 初始化人脸检测引擎
		NativeLong ret = FDLibrary.initFaceDetectionEngine(
				appid, fdSdkKey,FDWorkMem, FD_WORKBUF_SIZE,faceDetectionEngine,
				FdOrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT,MIN_FACE_RECOGNITION_RE_SIZE,
				MAX_FACE_NUM);
		// 判断人脸检测引擎是否启动成功，失败则直接清理内存，退出系统
		if (ret.longValue() != 0) {
			// 释放内存
			CLibrary.free(FDWorkMem);
			System.out.println(String.format("face detection init fail！ERROR--:0x%x", ret.longValue()));
			// 关闭系统
			System.exit(0);
		}
		FDEngine = faceDetectionEngine.getValue();
		// 获取版本
		SdkVersion version = FDLibrary.getVersion(FDEngine);
		System.out.println(version.getVersion());
	}
	
	/**
	 * 获得人脸检测引擎
	 * @return 引擎
	 */
	public Pointer getFDEngine(){
		return FDEngine;
	}
		
	
	/**
	 * 人脸检测
	 * @param inputImg 图片
	 * @return 将图转换成ASVLOFFSCREEN
	 */
	public FaceInfo[] doFaceDetection(ASVLOFFSCREEN inputImg) {
		// 设置存储人脸位置的数组
		FaceInfo[] faceInfo = new FaceInfo[0];
		// 人脸检测结果
		PointerByReference ppFaceRes = new PointerByReference();
		// 判断是否成功调用方法
		NativeLong ret = FDLibrary.faceDetection(FDEngine, inputImg, ppFaceRes);
		if (ret.longValue() != 0) {
			System.out.println(String.format("face detection check fail！ ERROR--:0x%x", ret.longValue()));
			return faceInfo;
		}
		// 将人脸检测结果存入人脸存储数组
		Faceres faceRes = new Faceres(ppFaceRes.getValue());
		// 循环将位置存入数组
		if (faceRes.nFace > 0) {
			faceInfo = new FaceInfo[faceRes.nFace];
			for (int i = 0; i < faceRes.nFace; i++) {
				MRect rect = new MRect(new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
				int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
				faceInfo[i] = new FaceInfo();
				faceInfo[i].left = rect.left;
				faceInfo[i].top = rect.top;
				faceInfo[i].right = rect.right;
				faceInfo[i].bottom = rect.bottom;
				faceInfo[i].orient = orient;
			}
		}
		return faceInfo;
	}
	
	/**
	 * 销毁引擎,释放相应资源
	 */
	public void uninitialFaceEngine() {
		if(FDEngine!=null){
			// 销毁引擎
			FDLibrary.uninitialFaceEngine(FDEngine);
			// 释放占用的内存
			CLibrary.free(FDWorkMem);
		}
	}
	
	
	/**
	 * 测试启动引擎
	 * @param args
	 */
	public static void main(String[] args) {
		new FaceDetectedEngine();
	}
}
