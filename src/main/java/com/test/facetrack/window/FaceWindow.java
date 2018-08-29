package com.test.facetrack.window;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.SystemColor;
/**
 * 人脸追踪窗口
 */
public class FaceWindow {
	// Frame窗口
	private JFrame frame;
	// Frame大小设置
	private int jFwidth = 900 ,jFheight = 600;
	// 视频容器
	private JLabel videlJLabel;
	// 信息容器
	private JPanel infoJPanel;
	// 圆圈颜色
	private Color color;
	// 圆圈位置
	private int x,y,width,height;
	
	/**
	 * 构造方法,执行初始化
	 */
	public FaceWindow() {
		initialize();
	}
	/**
	 * 构造方法,设置窗口大小,执行初始化
	 */
	public FaceWindow(int jFwidth,int jFheight) {
		this.jFwidth = jFheight;
		this.jFheight = jFheight;
		initialize();
	}

	/**
	 * 获得Frame
	 * @return
	 */
	public JFrame getFrame() {
		return frame;
	}
	/**
	 * 将当前帧加入到视频JLabel
	 * @param img 头像图片Buffered
	 */
	public void setVideo(BufferedImage img) {
		ImageIcon icon = new ImageIcon(img);
		videlJLabel.setIcon(icon);	
	}
	/**
	 * 获得信息容器
	 * @return infoJPanel
	 */
	public JPanel getInfoJP() {
		return infoJPanel;
	}
	
	/**
	 * 设置圆圈颜色
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
		infoJPanel.updateUI();
	}
	
	/**
	 * 设置圆圈位置和大小
	 * @param x x坐标位置
	 * @param y y坐标位置
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setOval(int x,int y,int width,int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		infoJPanel.updateUI();
	}
	
	/**
	 * 初始化窗口内容
	 */
	private void initialize() {
		// 设置窗体
		frame = new JFrame();
		frame.setBounds(0, 0, jFwidth, jFheight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		// 设置窗口启动后,根据系统分辨率大小设置居中显示
		int frameWidth = (Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2;
		int frameHeight = (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2;
		frame.setLocation(frameWidth, frameHeight);

		// 视频区域
		JPanel videoJPanel = new JPanel();
		videoJPanel.setBackground(Color.CYAN);
		videoJPanel.setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(videoJPanel, BorderLayout.CENTER);

		videlJLabel = new JLabel();
		videoJPanel.add(videlJLabel);

		// 信息区域
		infoJPanel = new JPanel() {
			private static final long serialVersionUID = -8843812097304572932L;
			public void paint(Graphics g){
				// 调用父类函数完成初始化任务
				super.paint(g);
				g.setColor(color);
				// 先画出一个圆圈
				g.fillOval(x, y, width, height);
			}
		};
		infoJPanel.setBackground(SystemColor.controlDkShadow);
		frame.getContentPane().add(infoJPanel, BorderLayout.EAST);
		infoJPanel.setLayout(null);
		infoJPanel.setPreferredSize(new Dimension(300, 0));
		
	}

	/**
	 * 测试窗口
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		final FaceWindow window = new FaceWindow();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 显示窗口
					window.frame.setVisible(true);
					// 测试绘制的颜色
					window.setColor(Color.red);
					// 测试绘制实心圆
					window.setOval(100,100,80,80);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// 设置等待3秒
		Thread.sleep(3000);
		// 3秒后测试绘制的颜色
		window.setColor(Color.green);
		window.getInfoJP().updateUI();
	}

}
