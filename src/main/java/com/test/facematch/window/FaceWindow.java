package com.test.facematch.window;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import com.test.facematch.utils.ImageLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.SystemColor;
/**
 * 人脸检测、对比窗口
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
	// 名字
	private JLabel nameJLabel2;
	// 号码
	private JLabel numJLabel2;
	// 头像
	private JLabel headImgJLabel;
	
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
	 * 设置名字
	 * @param name
	 */
	public void setName(String name) {
		nameJLabel2.setText(name);
		nameJLabel2.updateUI();
	}
	
	/**
	 * 设置号码
	 * @param num
	 */
	public void setNum(String num) {
		numJLabel2.setText(num);
		numJLabel2.updateUI();
	}
	
	/**
	 * 设置文件名字
	 * @param fileName 文件名
	 */
	public void setHead(String fileName) {
		ImageIcon img = ImageLoader.getImageFromFile(fileName, headImgJLabel.getWidth(),
				headImgJLabel.getHeight());;
		headImgJLabel.setIcon(img);
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
			private static final long serialVersionUID = -8843812097304472932L;
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

		// 头像区域
		headImgJLabel = new JLabel("icon");
		headImgJLabel.setBackground(Color.DARK_GRAY);
		headImgJLabel.setBounds(0, 0, 125, 136);
		int iconWidth = ((int) infoJPanel.getPreferredSize().getWidth() - headImgJLabel.getWidth()) / 2;
		int iconHeight = 60;
		headImgJLabel.setLocation(iconWidth, iconHeight);
		ImageIcon img = ImageLoader.getImageFromFile("./images/bg.png", headImgJLabel.getWidth(),
				headImgJLabel.getHeight());
		headImgJLabel.setIcon(img);
		headImgJLabel.setBorder(new LineBorder(new Color(40, 40, 40)));
		infoJPanel.add(headImgJLabel);

		// 名字区域
		JPanel nameJPanel = new JPanel();
		FlowLayout nameflowLayout = (FlowLayout) nameJPanel.getLayout();
		nameflowLayout.setAlignment(FlowLayout.LEFT);
		nameJPanel.setOpaque(false);
		nameJPanel.setBackground(Color.LIGHT_GRAY);
		nameJPanel.setBounds(0, 0, 120, 35);
		int nameWidth = ((int) infoJPanel.getPreferredSize().getWidth() - nameJPanel.getWidth()) / 2;
		int nameHeight = 210;
		nameJPanel.setLocation(nameWidth, nameHeight);
		infoJPanel.add(nameJPanel);

		JLabel nameJLabel1 = new JLabel("姓名:");
		nameJLabel1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		nameJLabel1.setForeground(new Color(200, 200, 200));
		nameJPanel.add(nameJLabel1, BorderLayout.WEST);
		nameJLabel2 = new JLabel("");
		nameJLabel2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		nameJLabel2.setForeground(new Color(250, 250, 250));
		nameJPanel.add(nameJLabel2, BorderLayout.CENTER);

		// 号码区域
		JPanel numJPanel = new JPanel();
		FlowLayout numflowLayout = (FlowLayout) numJPanel.getLayout();
		numflowLayout.setAlignment(FlowLayout.LEFT);
		numJPanel.setOpaque(false);
		numJPanel.setBackground(Color.LIGHT_GRAY);
		numJPanel.setBounds(0, 0, 120, 35);
		int numWidth = ((int) infoJPanel.getPreferredSize().getWidth() - numJPanel.getWidth()) / 2;
		int numHeight = 260;
		numJPanel.setLocation(numWidth, numHeight);
		infoJPanel.add(numJPanel);

		JLabel numJLabel1 = new JLabel("号码:");
		numJLabel1.setFont(new Font("微软雅黑", Font.BOLD, 16));
		numJLabel1.setForeground(new Color(200, 200, 200));
		numJPanel.add(numJLabel1, BorderLayout.WEST);
		numJLabel2 = new JLabel("");
		numJLabel2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		numJLabel2.setForeground(new Color(250, 250, 250));
		numJPanel.add(numJLabel2, BorderLayout.CENTER);
		
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
					window.setOval(120,340,80,80);
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
