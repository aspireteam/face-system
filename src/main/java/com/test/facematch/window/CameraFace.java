package com.test.facematch.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import com.test.facematch.engine.FaceDetectedEngine;
import com.test.facematch.engine.FaceMatchingEngine;
import com.test.facematch.entity.ASVLOFFSCREEN;
import com.test.facematch.entity.FaceInfo;
import com.test.facematch.entity.FaceModel;
import com.test.facematch.entity.User;
import com.test.facematch.utils.ImageConvert;
/**
 * OpenCv启动摄像头，进行人脸检测
 */
public class CameraFace {
	// 人脸比对引擎
	private FaceMatchingEngine faceMatchingEngine;
	// 人脸检测引擎
	private FaceDetectedEngine faceDetectedEngine;
	// 用户数据存储
	private List<User> userDataList;
	// 设置匹配分数,超过分数就为匹配成功
	public static final Float MATCH_SCORE =0.6f;
	// 视频循环开关
	static boolean flag=true;
	// 人脸检测线程池
	ExecutorService faceCheckExecutors = Executors.newCachedThreadPool();
	// 人脸对比线程池
	ExecutorService faceMatchingExecutors = Executors.newCachedThreadPool();
	// 用户信息队列
	ConcurrentLinkedQueue<User> userQueue = new ConcurrentLinkedQueue<User>();
	// 人脸位置队列
	ConcurrentLinkedQueue<FaceInfo[]> faceInfoQueue = new ConcurrentLinkedQueue<FaceInfo[]>();
	
	/**
	 * 使用OpenCv前必须先调用此方法载入OpenCv依赖库
	 */
	static{ 
		System.loadLibrary("libs/"+Core.NATIVE_LIBRARY_NAME);
	}
	
	/**
	 * 设置人脸比对引擎
	 * @param FREngine
	 */
	public void setFREngine(FaceMatchingEngine FREngine) {
		this.faceMatchingEngine = FREngine;
	}
	
	/**
	 * 设置人脸检测引擎
	 * @param FREngine
	 */
	public void setFDEngine(FaceDetectedEngine FDEngine) {
		this.faceDetectedEngine = FDEngine;
	}
	
	/**
	 * 设置用户数据集合
	 * @param userDataList 用户数据集合
	 */
	public void setUserData(List<User> userDataList) {
		this.userDataList = userDataList;
	}

	/**
	 * 加载人脸检测引擎、窗口,启动摄像头，人脸辨别
	 */
	public void Start() {
		// 启动窗口
		final FaceWindow window = new FaceWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.getFrame().setVisible(true);
					// 设置实心圆初始大小
					window.setOval(120,350,80,80);
					// 设置实心圆初始颜色
					window.setColor(Color.WHITE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// OepnCv摄像头camera操作对象
		VideoCapture camera=new VideoCapture();
		/** 
		 * 打开摄像头
		 */
		camera.open(0);
		// 判断摄像头是否被打开，如果打开失败则抛出异常
		if(!camera.isOpened()){
			throw new RuntimeException("Camera Start Error");
		}
		// 设置用于存储用户的对象
		User user;
		// 用于存储人脸位置的对象
		FaceInfo[] faceInfos;
		// 创建时间间隔对象
		long timePre = System.currentTimeMillis();
		long timeLast = 0;
		long faceCheckTime = System.currentTimeMillis();
		// 摄像头打开成功,则循环抓取帧输出
		Mat mat=new Mat();
		while(flag){
			// 读取1帧
			camera.read(mat);
			// 设置最后时间为当前时间
			timeLast = System.currentTimeMillis();
			// 判读是否为空,为空则结束此次循环
			if(mat.empty()){
				continue;
			}
			// 设置视频画面大小缩放
			//float width=mat.width()/(mat.width()/1516.0f);
			//float height=mat.height()/(mat.height()/798.0f);
			//Size size = new Size(width,height);
			// 缩放矩阵
			//Imgproc.resize(mat, mat, size);
			// 将mat转换成BufferedImage
			BufferedImage img = ImageConvert.mat2BI(mat);
			// 将BufferdImage转换成ASVLOFFSCREEN
			ASVLOFFSCREEN asv = ImageConvert.imageConvertASVLOFFSCREEN(img);
			// 判断间隔,每隔20ms执行一次人脸检测
			if(timeLast - faceCheckTime > 20) {
				executeFaceCheck(asv);
			}
			// 绘制图形
			if(!faceInfoQueue.isEmpty()) {
				// 取出位置信息
				faceInfos = faceInfoQueue.poll();
				// 从队列取出位置信息绘制图
				for(FaceInfo fInfo:faceInfos) {
					// 设置圆圈颜色为绿色
					window.setColor(Color.GREEN);
					// 绘制矩形
					drawRect(img,fInfo);
					// 绘制名字
					drawName(img,fInfo,"测试名字");
					// ========判断是否要抓画面进行人脸识别,如果距离上次大于500ms就进行人脸分析======
					if(timeLast - timePre > 500){
						System.out.println("--执行--");
						// 设置时间为结束时间，方便下次判断
						timePre = System.currentTimeMillis();
						// 执行人脸匹配
						executeFaceMatch(asv,fInfo);
					}
				}
			}else {
				// 如果未检测到人脸则设置圆圈为浅红
				window.setColor(Color.PINK);
			}
			// 从队列取出用户信息输出到界面
			if(!userQueue.isEmpty()) {
				// 将用户信息取出
				user = userQueue.poll();
				// 设置辨别的人物信息
				window.setName(user.getName());
				window.setNum(user.getNum());
				// 设置头像
				window.setHead(user.getImage());
			}
			// 将这帧绑定到label上输出到界面
			window.setVideo(img);
		}
		// 清理摄像头资源
		camera.release();
		// 销毁引擎,释放相应资源
		faceDetectedEngine.uninitialFaceEngine();
		faceMatchingEngine.uninitialFaceEngine();
	}
	
	/**
	 * 绘制矩形
	 * @param img
	 * @param faceInfo
	 */
	private void drawRect(BufferedImage img,FaceInfo faceInfo) {
		// 创建画笔
		Graphics g = img.getGraphics();
		// 画笔颜色
		g.setColor(Color.RED);
		// 绘制矩形框(原点x坐标,原点y坐标,矩形的长,矩形的宽)
		g.drawRect(faceInfo.getLeft(), faceInfo.getTop(),
				faceInfo.getRight() - faceInfo.getLeft(),
				faceInfo.getBottom() - faceInfo.getTop());
	}

	/**
	 * 绘制名字
	 * @param img
	 * @param faceInfo
	 * @param name
	 */
	private void drawName(BufferedImage img,FaceInfo faceInfo,String name) {
		// 创建画笔
		Graphics g = img.getGraphics();
		// 画笔颜色
		g.setColor(Color.RED);
		// 字体大小
		int fontSize = 20;
		//设置字体
		g.setFont(new Font("黑体",Font.BOLD,fontSize));
		//设置颜色
		g.setColor(Color.ORANGE);
		// 计算字体要显示的位置+fontSize*name.length()
		int x = faceInfo.getLeft()+(faceInfo.getRight()-faceInfo.getLeft()-fontSize*name.length())/2;
		int y = faceInfo.getTop();
		// 绘制名字
		g.drawString(name, x, y);	
	}
	
	/**
	 * 增加新线程来执行人脸比对
	 * @param inputImg
	 * @param faceInfo
	 */
	public void executeFaceMatch(final ASVLOFFSCREEN inputImg, final FaceInfo faceInfo) {
		// 加入线程池
		faceCheckExecutors.submit(new Runnable() { 	
			public void run() {
				// 分析特征值
				FaceModel faceModel = faceMatchingEngine.extractFRFeature(inputImg, faceInfo);
				if(faceModel!=null) {
					// 设置分数
					float score = 0;
					// 循环比较特征值
					for(User user:userDataList) {
						score = faceMatchingEngine.compareFaceSimilarity(faceModel, user.getFaceModel());
						if(score > MATCH_SCORE) {
							// 将信息加入队列，在摄像头输出时候将出队将信息输出
							userQueue.add(user);
							// 结束循环
							break;
						}
					}
					// 释放特征值占用的空间
					faceModel.freeUnmanaged();
				}
			}
		});
	}
	
	/**
	 * 增加新线程来执行人脸检测
	 * @param inputImg
	 */
	public void executeFaceCheck(final ASVLOFFSCREEN inputImg) {
		// 人脸检测,因为一张图可能有多张脸,所以为数组形式
		FaceInfo[] faceInfos = faceDetectedEngine.doFaceDetection(inputImg);
		if(faceInfos!=null && faceInfos.length > 0) {
			// 检测出人脸则存入队列
			faceInfoQueue.add(faceInfos);	
		}
	}

}