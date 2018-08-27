package com.test.facecheck.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import com.test.facecheck.engine.FaceDetectedEngine;
import com.test.facecheck.entity.ASVLOFFSCREEN;
import com.test.facecheck.entity.FaceInfo;
import com.test.facecheck.utils.ImageConvert;
/**
 * OpenCv启动摄像头，进行人脸检测
 */
public class CameraFace {

	// 视频循环开关
	static boolean flag=true;
	
	/**
	 * 使用OpenCv前必须先调用此方法载入OpenCv依赖库
	 */
	static{ 
		System.loadLibrary("libs/"+Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * 加载人脸检测引擎、窗口，启动摄像头
	 */
	public void Start(FaceDetectedEngine faceCheckEngine) {
		// 启动窗口
		final FaceWindow window = new FaceWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// OepnCv摄像头camera操作对象
		VideoCapture camera=new VideoCapture();
		/** 
		 * 打开摄像头
		 * open函数中的0代表当前计算机中索引为0的摄像头，如果你的计算机有多个摄像头，那么一次1,2,3……
		 *  - 打开本机摄像头camera.open(0);
		 *  - 打开远程摄像头camera.open("rtsp://username:password@10.8.110.123/Streaming/channels/1/");
		 */
		//camera.open(0);
		camera.open("rtsp://admin:asp@2018@10.8.116.9/Streaming/channels/1/");
		// 判断摄像头是否被打开，如果打开失败则抛出异常
		if(!camera.isOpened()){
			throw new RuntimeException("Camera Start Error");
		}
		// 摄像头打开成功，则循环抓取帧输出
		Mat mat=new Mat();
		while(flag){
			// 读取1帧
			camera.read(mat);
			// 设置检测圆圈为灰色
			window.setColor(Color.GRAY);
			// 将mat转换成BufferedImage
			BufferedImage img = ImageConvert.mat2BI(mat);
			// 将BufferdImage转换成ASVLOFFSCREEN
			ASVLOFFSCREEN asv = ImageConvert.imageConvertASVLOFFSCREEN(img);
			// 人脸检测
			FaceInfo[] faceInfos = faceCheckEngine.doFaceDetection(asv);
			// 如果未检测到人脸则设置圆圈为灰色
			if(faceInfos.length==0) {
				window.setColor(Color.GRAY);
			}else {
				// 如果检测到则在人脸上画框
				for (FaceInfo faceinfo : faceInfos) {
					// 创建画笔
					Graphics g = img.getGraphics();
					// 画笔颜色
					g.setColor(Color.RED);
					// 绘制矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
					g.drawRect(faceinfo.getLeft(), faceinfo.getTop(),
							faceinfo.getRight() - faceinfo.getLeft(),
							faceinfo.getBottom() - faceinfo.getTop());
					// 绘制名字
					g.setFont(new Font("黑体",Font.BOLD,20));//设置字体
					g.setColor(Color.BLACK);//设置颜色
					g.drawString("测试名字", faceinfo.getLeft(), faceinfo.getTop());	
					// 设置颜色为绿色
					window.setColor(Color.GREEN);
				}
			}
			// 将这帧绑定到label上输出到界面
			window.setVideo(img);
		}
		// 清理摄像头资源
		camera.release();
		// 销毁引擎，释放相应资源
		faceCheckEngine.uninitialFaceEngine();
	}
    
	

}