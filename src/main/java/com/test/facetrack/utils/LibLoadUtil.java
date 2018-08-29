package com.test.facetrack.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
/**
 * 动态链接库引入工具
 */
public class LibLoadUtil {

	/**
	 * 通过JNA载入库
	 * @param filePath 文件路径
	 * @param interfaceClass 类名称
	 * @return
	 */
	public static <T> T loadLibrary(String filePath, Class<T> interfaceClass) {
		if (Platform.isWindows()) {
			filePath = filePath.endsWith(".dll") ? filePath : filePath + ".dll";
		} else if (Platform.isLinux()) {
			filePath = filePath.endsWith(".so") ? filePath : filePath + ".so";
		}
		return Native.loadLibrary(filePath, interfaceClass);
	}

	/**
	 * 测试JNA引用
	 * 一般情况下，需要自己写一个接口，把引入的动态库中的方法写出来，例如下面引入c语言的库，并重写了printf方法
	 */
	public interface CLibrary extends Library {
		CLibrary INSTANCE = loadLibrary((Platform.isWindows() ? "msvcrt" : "c") ,CLibrary.class);
		
		void printf(String format, Object... args);
	}
	
	
	public static void main(String[] args) {
		// 测试通过JNA引入c语言的方法，成功输出则引入成功
		CLibrary.INSTANCE.printf("haaha %d", 4);
	}
}
