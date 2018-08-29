package com.test.facecheck.libs;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.test.facecheck.entity.ASVLOFFSCREEN;
import com.test.facecheck.entity.SdkVersion;
import com.test.facecheck.utils.LibLoadUtil;
/**
 * 人脸检测依赖引入
*/
public class FaceDetectionLibrary {
	/**
	 * 人脸检测依赖
	 */
	private interface FaceRecognition extends Library{
		// 加载人脸检测库
		FaceRecognition INSTANCE = LibLoadUtil.loadLibrary("libs/libarcsoft_fsdk_face_detection",FaceRecognition.class);
		
		/*实例化库中的方法 */
		// 初始化脸部检测引擎
		NativeLong AFD_FSDK_InitialFaceEngine(
				String appid, String sdkid, 
				Pointer pWorkMem, int pWorkMemSize,
				PointerByReference phEngine, int orientPriority,
				int nScale, int nMaxFace);
		// 根据输入的图像检测出人脸位置,一般用于静态图像检测
		NativeLong AFD_FSDK_StillImageFaceDetection(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference faceRes);
		// 获取SDK版本信息
		SdkVersion AFD_FSDK_GetVersion(Pointer hEngine);
		// 销毁引擎,释放相应资源
		NativeLong AFD_FSDK_UninitialFaceEngine(Pointer hEngine);
	}
	
	/**
	 * 初始化脸部检测引擎
	 * @param appid 用户申请SDK时获取的App Id
	 * @param sdkid 用户申请SDK时获取的SDK Key
	 * @param pWorkMem 分配给引擎使用的内存地址
	 * @param pWorkMemSize 分配给引擎使用的内存大小
	 * @param phEngine 引擎handle
	 * @param OrientPriority 期望的脸部检测角度范围
	 * @param nScale [in] 用于数值表示的最小人脸尺寸 有效值范围[2,50] 推荐值 16
	 * 。该尺寸是人脸相对于所在图片的长边的占比。例如,如果用户想检测到的最小人脸尺寸是
	 * 图片长度的1/8,那么这个nScale就应该设置为8
	 * @param nMaxFace 最多能检测到的人脸个数,有效值范围[1,50],推荐值25
	 * @return 成功返回MOK,否则返回失败code。失败codes如下所列
	 */
	public NativeLong initFaceDetectionEngine(String appid, String sdkid, Pointer pWorkMem, int pWorkMemSize,
			PointerByReference phEngine, int orientPriority, int nScale, int nMaxFace){
		return FaceRecognition.INSTANCE.AFD_FSDK_InitialFaceEngine(appid, sdkid, pWorkMem,
				pWorkMemSize, phEngine, orientPriority, nScale, nMaxFace);
	}
	
	/**
	 * 根据输入的图像检测出人脸位置,一般用于静态图像检测
	 * @param hEngine 引擎handle
	 * @param pImgData 待检测的图像信息
	 * @param faceRes 人脸检测结果
	 * @return 成功返回MOK,否则返回失败code
	 */
	public NativeLong faceDetection(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference faceRes){
		return FaceRecognition.INSTANCE.AFD_FSDK_StillImageFaceDetection(hEngine ,pImgData ,faceRes);
	}
	
	/**
	 * 获取SDK版本信息
	 * @param hEngine 引擎handle
	 * @return
	 */
	public SdkVersion getVersion(Pointer hEngine){
		return FaceRecognition.INSTANCE.AFD_FSDK_GetVersion(hEngine);
	}
	
	/**
	 * 销毁引擎,释放相应资源
	 * @param hEngine 引擎handle
	 * @return 成功返回MOK,否则返回失败code。失败codes如下所列
	 */
	public NativeLong uninitialFaceEngine(Pointer hEngine){
		return FaceRecognition.INSTANCE.AFD_FSDK_UninitialFaceEngine(hEngine);
	}

	
}
