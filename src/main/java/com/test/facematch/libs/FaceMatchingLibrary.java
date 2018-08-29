package com.test.facematch.libs;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;
import com.test.facematch.entity.ASVLOFFSCREEN;
import com.test.facematch.entity.FRSdkVersion;
import com.test.facematch.utils.LibLoadUtil;
import com.test.facematch.entity.FaceInput;
import com.test.facematch.entity.FaceModel;
/**
 * 人脸比对依赖引入
*/
public class FaceMatchingLibrary {
	/**
	 * 人脸比对依赖
	 */
	private interface FaceRecognition extends Library{
		// 加载人脸比对库
		FaceRecognition INSTANCE = LibLoadUtil.loadLibrary("libs/libarcsoft_fsdk_face_recognition",FaceRecognition.class);
		
		/*实例化库中的方法 */
		// 初始化人脸比对引擎
		NativeLong AFR_FSDK_InitialEngine(String appid, String sdkid, Pointer pWorkMem, int pWorkMemSize,
				PointerByReference phEngine);
		
		// 获取脸部特征参数
	    NativeLong AFR_FSDK_ExtractFRFeature(Pointer hEngine,ASVLOFFSCREEN pImgData,         
	            FaceInput pFaceRes,FaceModel pFaceModels); 
	    
	    // 脸部特征比较
	    NativeLong AFR_FSDK_FacePairMatching(Pointer hEngine, FaceModel reffeature,    
	            FaceModel probefeature,FloatByReference pfSimilScore);
		
		// 获取SDK版本信息
		FRSdkVersion AFR_FSDK_GetVersion(Pointer hEngine);
		
		// 销毁引擎,释放相应资源
		NativeLong AFR_FSDK_UninitialEngine(Pointer hEngine);
	}
	
	/**
	 * 初始化脸部检测引擎
	 * @param appid 用户申请SDK时获取的APPId
	 * @param sdkid 用户申请SDK时获取的SDK-Key
	 * @param pWorkMem 分配给引擎使用的内存地址
	 * @param pWorkMemSize 分配给引擎使用的内存大小
	 * @param phEngine 引擎handle
	 */
	public NativeLong initFaceMatchingEngine(String appid, String sdkid, Pointer pWorkMem, int pWorkMemSize,
			PointerByReference phEngine){
		return FaceRecognition.INSTANCE.AFR_FSDK_InitialEngine(appid, sdkid, pWorkMem,pWorkMemSize, phEngine);
	}
	
	/**
	 * 获取脸部特征参数
	 * @param hEngine 引擎handle
	 * @param pImgData 输入的图像数据
	 * @param pFaceRes 已检测到的脸部信息
	 * @param pFaceModels 提取的脸部特征信息
	 * @return
	 */
	public NativeLong faceExtractFRFeature(Pointer hEngine,ASVLOFFSCREEN pImgData,FaceInput pFaceRes,FaceModel pFaceModels){
		return FaceRecognition.INSTANCE.AFR_FSDK_ExtractFRFeature(hEngine ,pImgData ,pFaceRes,pFaceModels);
	}
	
	/**
	 * 脸部特征比较
	 * @param hEngine 引擎handle
	 * @param reffeature 已有脸部特征信息
	 * @param probefeature 被比较的脸部特征信息
	 * @param pfSimilScore 脸部特征相似程度数值
	 * @return
	 */
	public NativeLong facePairMatchinge(Pointer hEngine, FaceModel reffeature,FaceModel probefeature
			,FloatByReference pfSimilScore){
		return FaceRecognition.INSTANCE.AFR_FSDK_FacePairMatching(hEngine ,reffeature ,probefeature,pfSimilScore);
	}
   
	
	/**
	 * 获取SDK版本信息
	 * @param hEngine 引擎handle
	 * @return
	 */
	public FRSdkVersion getVersion(Pointer hEngine){
		return FaceRecognition.INSTANCE.AFR_FSDK_GetVersion(hEngine);
	}
	
	/**
	 * 销毁引擎,释放相应资源
	 * @param hEngine 引擎handle
	 * @return 成功返回MOK,否则返回失败code。失败codes如下所列
	 */
	public NativeLong uninitialFaceEngine(Pointer hEngine){
		return FaceRecognition.INSTANCE.AFR_FSDK_UninitialEngine(hEngine);
	}
	
}
