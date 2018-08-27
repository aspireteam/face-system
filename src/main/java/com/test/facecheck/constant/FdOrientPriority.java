package com.test.facecheck.constant;
/**
 * 定义脸部角度的检测范围 
 */
public class FdOrientPriority {
	// 检测0度（±45度）方向
	public static final int AFD_FSDK_OPF_0_ONLY = 0x1; 
	// 检测90度（±45度）方向
	public static final int AFD_FSDK_OPF_90_ONLY = 0x2; 
	// 检测270度（±45度）方向
	public static final int AFD_FSDK_OPF_270_ONLY = 0x3; 
	// 检测180度（±45度） 方向
	public static final int AFD_FSDK_OPF_180_ONLY = 0x4;
	// 检测0度，90度，180度，270度四个方向,其中0度更优先
	public static final int AFD_FSDK_OPF_0_HIGHER_EXT = 0x5;
}
