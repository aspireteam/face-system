package com.test.facetrack.constant;
/**
 *  定义脸部检测角度的优先级 
 */
public class FtOrientPriority {
	// 检测0度（±45度）方向
	public static final int AFT_FSDK_FOC_0 = 0x1;
	// 检测90度（±45度）方向
	public static final int AFT_FSDK_FOC_90 = 0x2; 
	// 检测270度（±45度）方向
	public static final int AFT_FSDK_FOC_270 = 0x3; 
	// 检测180度（±45度） 方向
	public static final int AFT_FSDK_FOC_180 = 0x4; 
	// 检测0度，90度，180度，270度四个方向,其中0度更优先
	public static final int AFT_FSDK_OPF_0_HIGHER_EXT = 0x5;
}
