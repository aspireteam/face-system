package com.test.facetrack.utils;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.test.facetrack.constant.AsvlColorFormat;
import com.test.facetrack.entity.ASVLOFFSCREEN;
import com.test.facetrack.entity.BufferInfo;
/**
 * 图片转换工具
 */
public class ImageConvert {
	
	/**
	 * 将BufferedImage转换成ASVLOFFSCREEN
	 * @param img 图片缓冲
	 * @return
	 */
	public static ASVLOFFSCREEN imageConvertASVLOFFSCREEN(BufferedImage img) {
		ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

		BufferInfo bufferInfo = ImageLoader.getBGRFromBuffImg(img);
		if (bufferInfo == null) {
			return null;
		}
		inputImg.u32PixelArrayFormat = AsvlColorFormat.ASVL_PAF_RGB24_B8G8R8;
		inputImg.i32Width = bufferInfo.width;
		inputImg.i32Height = bufferInfo.height;
		inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
		inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
		inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
		inputImg.ppu8Plane[1] = Pointer.NULL;
		inputImg.ppu8Plane[2] = Pointer.NULL;
		inputImg.ppu8Plane[3] = Pointer.NULL;

		inputImg.setAutoRead(false);
		return inputImg;
	}
	
	/**
	 * 将mat转换成BufferedImage
	 * @param mat
	 * @return
	 */
	public static BufferedImage mat2BI(Mat mat){
		int dataSize =mat.cols()*mat.rows()*(int)mat.elemSize();
		byte[] data=new byte[dataSize];
		mat.get(0, 0,data);
		int type=mat.channels()==1?
				BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;
		if(type==BufferedImage.TYPE_3BYTE_BGR){
			for(int i=0;i<dataSize;i+=3){
				byte blue=data[i+0];
				data[i+0]=data[i+2];
				data[i+2]=blue;
			}
		}
		BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}
 
}
