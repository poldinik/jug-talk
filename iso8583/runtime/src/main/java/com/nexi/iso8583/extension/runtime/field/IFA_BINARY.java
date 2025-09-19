package com.nexi.iso8583.extension.runtime.field;

import org.jpos.iso.ISOBinaryFieldPackager;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.LiteralBinaryInterpreter;
import org.jpos.iso.NullPrefixer;

/**
 * ISOFieldPackager Binary for ASCII connection
 *
 * @author diego pozzoli
 * @version $Id: IFA_BINARY.java 1 2009-05-24 11:44:00  $
 * @see ISOComponent
 * 
 * This binary field packager has been implemented to FIX the behavior of the 
 * standard jpos IFA_BINARY packager : binary fixed length fields are NOT 
 * unpacked into ASCII char pairs but are transported as RAW Binary DATA 
 */
public class IFA_BINARY extends ISOBinaryFieldPackager {
    public IFA_BINARY() {
        super(LiteralBinaryInterpreter.INSTANCE, NullPrefixer.INSTANCE);
    }
    
    /**
     * @param len - field length
     * @param description symbolic description
     */
    public IFA_BINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, NullPrefixer.INSTANCE);
    }

}
