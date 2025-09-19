package com.nexi.iso8583.extension.runtime.field;

import org.jpos.iso.ISOUtil;

import java.text.DecimalFormat;

public class ASCIITLLLVField extends ASCIITLVField{

	public ASCIITLLLVField() {}

	public ASCIITLLLVField(int tag, byte[] value) {
		super(tag,value);
	}

	public ASCIITLLLVField(int tag, String value) {
		super(tag,value);
	}

	@Override
	public byte[] pack() {
		DecimalFormat df = new DecimalFormat("000");
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

	@Override //this wasn't overridden in original implementation but ISO wants the length to be 3 bytes
	public int unpack(byte[] buf, int offset) {
		int consumed = 0;
		setTag(Integer.decode("0x"+ISOUtil.hexString(buf, offset + consumed, 2)));
		consumed += 2;
		int length = Integer.parseInt(new String(buf, offset + consumed, 3));
		consumed += 3;
		byte[] unpackedValue = new byte[length]; 
		System.arraycopy(buf, offset+ consumed, unpackedValue, 0, length);
		consumed += length;
		setValue(unpackedValue);
		return consumed;

	}
}
