package com.test.facematch.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.test.facematch.libs.CLibrary;
/**
 * 面部特征模型
 */
public class FaceModel extends Structure {

    public ByteByReference pbFeature;
    public int lFeatureSize;
    protected boolean bAllocByMalloc;
    
    @Override
    protected List<String> getFieldOrder() { 
    	List<String> list = new ArrayList<String>();
		list.add("pbFeature");
		list.add("lFeatureSize");
		return list;
    }
    
    public FaceModel deepCopy() throws Exception{
        
        if(!isValid()){
            throw new Exception("invalid feature");
        }
        // 创建对象并分配内存空间
        FaceModel feature = new FaceModel();
        feature.bAllocByMalloc = true; 
        feature.lFeatureSize = lFeatureSize;
        feature.pbFeature = new ByteByReference();
        feature.pbFeature.setPointer(CLibrary.malloc(feature.lFeatureSize));
        CLibrary.memcpy(feature.pbFeature.getPointer(),pbFeature.getPointer(),feature.lFeatureSize);
        return feature;
    }
    
    /**
     * // 释放内存空间
     */
    public synchronized void freeUnmanaged(){
        if(bAllocByMalloc&&isValid()){
            CLibrary.free(pbFeature.getPointer());
            pbFeature = null;
        }
    }
    
    /**
     * 执行销毁
     */
    @Override
    protected void finalize() throws Throwable  { 
        freeUnmanaged();
    }
    
    /**
     * 将byte数组转换成对象
     * @param byteArray
     * @return
     * @throws Exception
     */
    public static FaceModel fromByteArray(byte[] byteArray) throws Exception{
        if(byteArray == null){
            throw new Exception("invalid byteArray");
        }
        // 创建对象
        FaceModel feature = new FaceModel();
        feature.lFeatureSize = byteArray.length;
        feature.bAllocByMalloc = true; 
        feature.pbFeature = new ByteByReference();
        // 分配内存空间
        feature.pbFeature.setPointer(CLibrary.malloc(feature.lFeatureSize));
        // 将对象写入内存
        feature.pbFeature.getPointer().write(0, byteArray, 0, feature.lFeatureSize);
        return feature;
    }
    
    /**
     * 转换成字节数组
     * @return
     * @throws Exception
     */
    public byte[] toByteArray() throws Exception{
        if(!isValid()){
            throw new Exception("invalid feature");
        }
        return pbFeature.getPointer().getByteArray(0, lFeatureSize);
    }
    
    /**
     * 功能检查对象变量是否已经实例化
     * @return
     */
    private boolean isValid() {
        return ((pbFeature != null)&&(Pointer.nativeValue(pbFeature.getPointer())!= 0));
    }
}
