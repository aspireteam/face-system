package com.test.facetrack.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.test.facetrack.constant.FtOrientPriority;
import com.test.facetrack.entity.ASVLOFFSCREEN;
import com.test.facetrack.entity.MRect;
import com.test.facetrack.entity.FaceInfo;
import com.test.facetrack.entity.Faceres;
import com.test.facetrack.entity.SdkVersion;
import com.test.facetrack.libs.CLibrary;
import com.test.facetrack.libs.FaceTrackingLibrary;
/**
 * 人脸追踪引擎
 */
public class FaceTrackingdEngine {
	// AppId设置
	private String appid;
	// SDK-key设置
	private String ftSdkKey;
	// 分配给引擎使用的内存大小
	public static final int FT_WORKBUF_SIZE = 20 * 1024 * 1024;
	// 用户期望引擎最多能检测出的人脸数 有效值范围[1,50]
	public static final int MAX_FACE_NUM = 10;
	// 用于数值表示的最小人脸尺寸 有效值范围[2,50] 推荐值 16。该尺寸是人脸相对于所在图片的长边的占比。
	// 例如，如果用户想检测到的最小人脸尺寸是图片长度的1/8，那么这个nScale就应该设置为8
	public static final int MIN_FACE_RECOGNITION_RE_SIZE = 16;
	// 人脸追踪引擎
	private Pointer FTEngine;
	// 分配给引擎使用的内存地址
	private Pointer FTWorkMem;
	// 人脸追踪依赖
	private FaceTrackingLibrary FTLibrary;
	
	/**
	 * 构造方法,执行初始化
	 */
	public FaceTrackingdEngine(){
		// 初始化参数设置
		init();
		// 初始化人脸检测引擎
		initFaceTrackingEngine();
	}
	
	/**
	 * 初始化参数
	 */
	private void init() {
		try {
			// 分配给引擎使用的内存地址
			FTWorkMem = CLibrary.malloc(FT_WORKBUF_SIZE);
			// 创建Properties
			Properties properties = new Properties();
			// 加载properties文件
			InputStream is = FaceTrackingdEngine.class.getClassLoader().getResourceAsStream("application.properties");
			// 加载输入流
			properties.load(is);
			// 设置key和FD-SDKKEY
			appid = properties.getProperty("appid");
			ftSdkKey = properties.getProperty("ft_sdkkey");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化人脸追踪引擎
	 */
	private void initFaceTrackingEngine(){
		// 载入人脸追踪依赖库
		FTLibrary = new FaceTrackingLibrary();
		// 初始化人脸追踪引擎
		PointerByReference faceTrackingEngine = new PointerByReference();
		// 初始化人脸追踪引擎
		NativeLong ret = FTLibrary.initFaceDetectionEngine(
				appid, ftSdkKey,FTWorkMem, FT_WORKBUF_SIZE,faceTrackingEngine,
				FtOrientPriority.AFT_FSDK_OPF_0_HIGHER_EXT,MIN_FACE_RECOGNITION_RE_SIZE,
				MAX_FACE_NUM);
		// 判断人脸检测引擎是否启动成功，失败则直接清理内存，退出系统
		if (ret.longValue() != 0) {
			// 释放内存
			CLibrary.free(FTWorkMem);
			System.out.println(String.format("face tracting init fail！ERROR--:0x%x", ret.longValue()));
			// 关闭系统
			System.exit(0);
		}
		FTEngine = faceTrackingEngine.getValue();
		// 获取版本
		SdkVersion version = FTLibrary.getVersion(FTEngine);
		System.out.println(version.getVersion());
	}
	
	/**
	 * 获得人脸追踪引擎
	 * @return 引擎
	 */
	public Pointer getFTEngine(){
		return FTEngine;
	}
	
	/**
	 * 人脸位置追踪
	 * @param inputImg 待检测图像
	 * @return
	 */
	public FaceInfo[] doFaceTrace(ASVLOFFSCREEN inputImg) {
		// 设置存储人脸位置的数组
		FaceInfo[] faceInfo = new FaceInfo[0];
		// 人脸位置检测结果
		PointerByReference ppFaceRes = new PointerByReference();
		NativeLong ret = FTLibrary.faceFeatureDetect(FTEngine, inputImg, ppFaceRes);
		// 判断是否成功调用方法
		if (ret.longValue() != 0) {
			System.out.println(String.format("face feature detect fail！ ERROR--:0x%x", ret.longValue()));
			return faceInfo;
		}
		// 将人脸检测结果存入人脸存储数组
		Faceres faceRes = new Faceres(ppFaceRes.getValue());
		// 循环将位置存入数组
		if (faceRes.nFace > 0) {
			faceInfo = new FaceInfo[faceRes.nFace];
			for (int i = 0; i < faceRes.nFace; i++) {
				MRect rect = new MRect(new Pointer(Pointer.nativeValue(
						faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
				faceInfo[i] = new FaceInfo();
				faceInfo[i].left = rect.left;
				faceInfo[i].top = rect.top;
				faceInfo[i].right = rect.right;
				faceInfo[i].bottom = rect.bottom;
			}
		}
		return faceInfo;
	}
		
	
	/**
	 * 销毁引擎,释放相应资源
	 */
	public void uninitialFaceEngine() {
		if(FTEngine!=null){
			// 销毁引擎
			FTLibrary.uninitialFaceEngine(FTEngine);
			// 释放占用的内存
			CLibrary.free(FTWorkMem);
		}
	}
	
	
	/**
	 * 测试启动引擎
	 * @param args
	 */
	public static void main(String[] args) {
		new FaceTrackingdEngine();
	}
}
