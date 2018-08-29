package com.test.facematch.libs;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

/**
 * 调用系统库来操作内存
 */
public class CLibrary {

	/**
	 * 载入系统中的动态库，方便设置内存空间，以及清理内存
	 */
	public interface SystemLibrary extends Library {
		// 载入系统依赖
		SystemLibrary INSTANCE = Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), SystemLibrary.class);

		/*
		 * malloc的全称是memory allocation，中文叫动态内存分配，用于申请一块连续的指定大小的内存块区域以
		 * void*类型返回分配的内存区域地址，当无法知道内存具体位置的时候，想要绑定真正的内存空间，就需要用到动 态的分配内存。
		 */
		Pointer malloc(int len);
		// 释放了malloc所申请的内存
		void free(Pointer p);
		// 输出
		void printf(String format, Object... args);
		// memcpy函数的功能是从源src所指的内存地址的起始位置开始拷贝n个字节到目标dest所指的内存地址的起始位置中。
		Pointer memcpy(Pointer dst, Pointer src, long size);
	}

	/**
	 * 动态分配内存空间
	 * @param len 分配大小 
	 * @return 内存地址
	 */
	public static Pointer malloc(int len) {
		return SystemLibrary.INSTANCE.malloc(len);
	}

	/**
	 * 释放了malloc所申请的内存
	 * @param p 内存地址
	 */
	public static void free(Pointer p) {
		SystemLibrary.INSTANCE.free(p);
	}

	/**
	 * 输出
	 * @param format 输出内容
	 * @param args 内容中的变量
	 */
	public static void printf(String format, Object... args) {
		SystemLibrary.INSTANCE.printf(format,args);
	}
	
	/**
	 * 从源src所指的内存地址的起始位置开始拷贝n个字节到目标dest所指的内存地址的起始位置中
	 * @param dst 目标地址
	 * @param src 源地址
	 * @param size 大小
	 */
	public static void memcpy(Pointer dst, Pointer src, long size) {
		SystemLibrary.INSTANCE.memcpy(dst,src,size);
	}
}
