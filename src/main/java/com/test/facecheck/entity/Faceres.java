package com.test.facecheck.entity;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.test.facecheck.entity.MRect;
/**
 *  检测到的脸部信息
 */
public class Faceres extends Structure{
    public int nFace;
    public MRect.ByReference rcFace;
    public IntByReference lfaceOrient;

    public Faceres() {}
    public Faceres(Pointer p) {
        super(p);
        read();
    }
    
    public static class ByReference extends Faceres implements Structure.ByReference {
        public ByReference() {

        }
        public ByReference(Pointer p) {
            super(p);
        }
    };

    @Override
    protected List<String> getFieldOrder() {
    	List<String> list = new ArrayList<String>();
    	list.add("nFace");
    	list.add("rcFace");
    	list.add("lfaceOrient");
        return list;
    }
}
