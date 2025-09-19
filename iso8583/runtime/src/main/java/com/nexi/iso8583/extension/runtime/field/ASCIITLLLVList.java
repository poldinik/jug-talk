package com.nexi.iso8583.extension.runtime.field;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

import java.util.HashMap;
import java.util.Iterator;

public class ASCIITLLLVList {

	private HashMap<Integer, ASCIITLLLVField> tags;
	
	public ASCIITLLLVList() {
		tags = new HashMap<>();
	}
	
	public void unpack(byte[] buf) throws ISOException {
	    ASCIITLLLVField currentNode;
	    int consumed = 0;
	    while (consumed < buf.length){
	    	currentNode = new ASCIITLLLVField();
	    	consumed += currentNode.unpack(buf,consumed);
	    	add(currentNode);
	    }
	    if (consumed != buf.length) throw new ISOException("Invalid TLV Data in ISOField");
	}
	
	public byte[] pack(){
		byte [] buf = new byte[2000];
		int offset = 0;
		for (Iterator<ASCIITLLLVField> i = tags.values().iterator();
			 i.hasNext();
			 ){
			ASCIITLLLVField f = i.next();
			byte [] cb = f.pack();
			System.arraycopy(cb, 0, buf, offset, cb.length);
			offset+=cb.length;
		}
		byte [] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, offset);
		return result;
	}
	
	public void add(ASCIITLLLVField tlvToAppend) {
	    tags.put(Integer.valueOf(tlvToAppend.getTag()),tlvToAppend);
	}
	
	public void add(int tag, byte[] value) {
	    add(new ASCIITLLLVField(tag, value));
	}
	
	public void add(int tag, String value) {
	    add(new ASCIITLLLVField(tag, ISOUtil.hex2byte(value)));
	}
	
	public void delete(int tag) {
	    tags.remove(Integer.valueOf(tag));
	}
	
	public ASCIITLLLVField find(int tag) {
		return tags.get(Integer.valueOf(tag));
	}
	
	public ASCIITLLLVField index(int index) {
	    return tags.get(index);
	}
	
	public String getString(int tag) {
		ASCIITLLLVField msg = find(tag);
	    return msg != null ? msg.getStringValue() : null;
	}
	
	public byte[] get(int tag) {
		ASCIITLLLVField msg = find(tag);
	    return msg != null ? msg.getValue() : null;
	}

}
