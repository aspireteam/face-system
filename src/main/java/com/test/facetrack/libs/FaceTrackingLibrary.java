package com.test.facetrack.libs;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.test.facetrack.entity.ASVLOFFSCREEN;
import com.test.facetrack.entity.SdkVersion;
import com.test.facetrack.utils.LibLoadUtil;
/**
 * 人脸追踪依赖引入
 */
public class FaceTrackingLibrary {

	public interface FaceTracking extends Library {
		// 加载人脸追踪库
		FaceTracking INSTANCE = LibLoadUtil.loadLibrary("libs/libarcsoft_fsdk_face_tracking", FaceTracking.class);

		/* 实例化库中的方法 */
		// 初始化人脸追踪引擎
		NativeLong AFT_FSDK_InitialFaceEngine(String appid, String sdkid, Pointer pMem, int lMemSize,
				PointerByReference phEngine, int iOrientPriority, int nScale, int nMaxFaceNum);

		// 根据输入的图像检测出人脸位置,一般用于静态图像检测
		NativeLong AFT_FSDK_FaceFeatureDetect(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference pFaceRes);

		// 销毁引擎,释放相应资源
		NativeLong AFT_FSDK_UninitialFaceEngine(Pointer hEngine);

		// 获取SDK版本信息
		SdkVersion AFT_FSDK_GetVersion(Pointer hEngine);
	}
	
	/**
	 * 初始化人脸追踪引擎
	 * 
	 * @param appidAppId      用户申请SDK时获取的App Id
	 * @param sdkid           用户申请SDK时获取的SDK Key
	 * @param pMem            分配给引擎使用的内存地址
	 * @param lMemSize        分配给引擎使用的内存大小
	 * @param phEngine        引擎handle
	 * @param iOrientPriority 期望的脸部检测角度的优先级
	 * @param nScale          用于数值表示的最小人脸尺寸 有效值范围[2,16] 推荐值 16
	 * @param nMaxFaceNum     用户期望引擎最多能检测出的人脸数 有效值范围[1,20]
	 * @return
	 */
	public NativeLong initFaceDetectionEngine(String appid, String sdkid, Pointer pWorkMem, int pWorkMemSize,
			PointerByReference phEngine, int orientPriority, int nScale, int nMaxFace){
		return FaceTracking.INSTANCE.AFT_FSDK_InitialFaceEngine(appid, sdkid, pWorkMem,
				pWorkMemSize, phEngine, orientPriority, nScale, nMaxFace);
	}
	
	/**
	 *  人脸追踪,根据输入的图像检测出人脸位置
	 * @param hEngine  引擎handle
	 * @param pImgData 带检测图像信息
	 * @param faceRes 人脸追踪结果
	 * @return 成功返回MOK,否则返回失败code
	 */
	public NativeLong faceFeatureDetect(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference faceRes){
		return FaceTracking.INSTANCE.AFT_FSDK_FaceFeatureDetect(hEngine ,pImgData ,faceRes);
	}
	
	/**
	 * 获取SDK版本信息
	 * @param hEngine 引擎handle
	 * @return
	 */
	public SdkVersion getVersion(Pointer hEngine){
		return FaceTracking.INSTANCE.AFT_FSDK_GetVersion(hEngine);
	}
	
	/**
	 * 销毁引擎,释放相应资源
	 * @param hEngine 引擎handle
	 * @return 成功返回MOK,否则返回失败code。失败codes如下所列
	 */
	public NativeLong uninitialFaceEngine(Pointer hEngine){
		return FaceTracking.INSTANCE.AFT_FSDK_UninitialFaceEngine(hEngine);
	}

}