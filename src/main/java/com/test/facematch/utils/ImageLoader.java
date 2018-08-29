package com.test.facematch.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.test.facematch.entity.ASVLOFFSCREEN;
import com.test.facematch.entity.BufferInfo;
import java.io.File;
import java.io.IOException;
/**
 * 图像加载工具
 */
public class ImageLoader {

	/**
	 * 将BufferedImage存储进BufferInfo
	 * @param img BufferedImage
	 * @return BufferInfo
	 */
	public static BufferInfo getBGRFromBuffImg(BufferedImage img) {
		byte[] bgr = null;
		int width = 0;
		int height = 0;
		try {
			width = img.getWidth();
			height = img.getHeight();
			BufferedImage bgrimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			bgrimg.setRGB(0, 0, width, height, img.getRGB(0, 0, width, height, null, 0, width), 0, width);
			bgr = ((DataBufferByte) bgrimg.getRaster().getDataBuffer()).getData();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new BufferInfo(width, height, bgr);
	}
	
	/**
     * 从本地获取图片，并按照指定的压缩倍数进行调整
     * 
     * @param filePath
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    public static ImageIcon getImageFromFile(String filePath, int width , int height) {
    	ImageIcon result = null;
        try {
            BufferedImage img = ImageIO.read(new File(filePath));
            
			/* 原始图像的宽度和高度 */
			double wr = width * 1.0 / img.getWidth(); // 获取缩放比例
			double hr = height * 1.0 / img.getHeight();
			//double scale = wr >= hr ? wr : hr;

			AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);

			return new ImageIcon(ato.filter(img, null));
	    } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 读取图片
     * @param filePath
     * @return
     */
    public static ASVLOFFSCREEN getImageFromFile(String filePath) {
    	ASVLOFFSCREEN result = null;
        try {
        	File file = new File(filePath);
        	if(file!=null && file.exists()) {
        		result = ImageConvert.imageConvertASVLOFFSCREEN(ImageIO.read(file));
        	}
	    } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
   
    
}
