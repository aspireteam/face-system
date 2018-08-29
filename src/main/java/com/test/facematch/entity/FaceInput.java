package com.test.facematch.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
/**
 * 人脸位信息存储
 */
public class FaceInput extends Structure {
    
    public MRect.ByValue rcFace;
    public int lOrient;
    
    public FaceInput() {}
    public FaceInput(Pointer p) {
        super(p);
        read();
    }
    
    public static class ByReference extends FaceInput implements Structure.ByReference {
        public ByReference() {

        }
        public ByReference(Pointer p) {
            super(p);
        }
    };
    
    @Override
    protected List<String> getFieldOrder() { 
       	List<String> list = new ArrayList<String>();
    		list.add("rcFace");
    		list.add("lOrient");
    		return list;
    }
}
