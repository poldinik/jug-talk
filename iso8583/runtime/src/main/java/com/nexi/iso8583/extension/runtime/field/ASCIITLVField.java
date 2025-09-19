package com.nexi.iso8583.extension.runtime.field;

import org.jpos.iso.ISOUtil;

import java.text.DecimalFormat;

public class ASCIITLVField {
		protected 	int 	tag;
	    protected 	byte[] 	value;

	    public ASCIITLVField() {}

	    public ASCIITLVField(int tag, byte[] value) {
	        this.tag = tag;
	        this.value = value;
	    }

	    public ASCIITLVField(int tag, String value) {
	        this.tag = tag;
	        this.value = value.getBytes();
	    }

	    public int getTag() {
	        return tag;
	    }

	    public byte[] getValue() {
	        return value;
	    }

	    public void setTag(int tag) {
	        this.tag = tag;
	    }

	    public void setValue(byte[] newValue) {
	        this.value = newValue;
	    }

	    public String getStringValue() {
	        return new String(value);
	    }

	    public int unpack(byte[] buf, int offset) {
	    	int consumed = 0;
			setTag(Integer.decode("0x"+ISOUtil.hexString(buf, offset + consumed, 2)));
			consumed += 2;
			
			int length = Integer.parseInt(new String(buf, offset + consumed, 2));
			consumed += 2;
			byte[] unpackedValue = new byte[length]; 
			System.arraycopy(buf, offset+ consumed, unpackedValue, 0, length);
			consumed += length;
			setValue(unpackedValue);
			return consumed;
		}
		public byte[] pack() {
			DecimalFormat df = new DecimalFormat("00");
			byte[] 	bTag 	 = ISOUtil.hex2byte(Integer.toHexString(tag));
			int 	vlen 	 = value != null ? value.length : 0;
			byte[] 	bLen 	 = df.format(vlen).getBytes();
			int 	fieldLen = bTag.length + bLen.length + vlen;
			byte[] 	packed 	 = new byte[fieldLen];
			
            System.arraycopy(bTag, 0, packed, 0, bTag.length);
            System.arraycopy(bLen, 0, packed, bTag.length, bLen.length);
            if (null != value)
            	System.arraycopy(value, 0, packed, bTag.length + bLen.length,value.length);

			return packed;
		}
}
