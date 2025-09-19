package com.nexi.iso8583.extension.runtime.field;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

import java.util.HashMap;
import java.util.Iterator;

public class ASCIITLVListLongTags{
    
	private HashMap<Long, ASCIITLVFieldLongTags> tags;

	public ASCIITLVListLongTags() {
		tags = new HashMap<>();
    }

    public void unpack(byte[] buf) throws ISOException {
        ASCIITLVFieldLongTags currentNode;
        int consumed = 0;
        while (consumed < buf.length){
        	currentNode = new ASCIITLVFieldLongTags();
        	consumed += currentNode.unpack(buf,consumed);
        	add(currentNode);
        }
        if (consumed != buf.length) throw new ISOException("Invalid TLV Data in ISOField");
    }

    public byte[] pack(){
    	byte [] buf = new byte[2000];
    	int offset = 0;
    	for (Iterator<ASCIITLVFieldLongTags> i = tags.values().iterator();
    		 i.hasNext();
    		 ){
    		ASCIITLVFieldLongTags f = i.next();
    		byte [] cb = f.pack();
    		System.arraycopy(cb, 0, buf, offset, cb.length);
    		offset+=cb.length;
    	}
    	byte [] result = new byte[offset];
    	System.arraycopy(buf, 0, result, 0, offset);
    	return result;
    }
    
	public void add(ASCIITLVFieldLongTags tlvToAppend) {
        tags.put(Long.valueOf(tlvToAppend.getTag()),tlvToAppend);
    }
    
    public void add(Long tag, byte[] value) {
        add(new ASCIITLVFieldLongTags(tag, value));
    }
    
    public void add(int tag, String value) {
        add(new ASCIITLVFieldLongTags(tag, ISOUtil.hex2byte(value)));
    }

    public void delete(Long tag) {
        tags.remove(tag);
    }

    public ASCIITLVFieldLongTags find(Long tag) {
    	return tags.get(tag);
    }

    public ASCIITLVFieldLongTags index(Long index) {
        return tags.get(index);
    }

    public String getString(Long tag) {
    	ASCIITLVFieldLongTags msg = find(tag);
        return msg != null ? msg.getStringValue() : null;
    }
    
    public byte[] get(Long tag) {
    	ASCIITLVFieldLongTags msg = find(tag);
        return msg != null ? msg.getValue() : null;
    }
}
