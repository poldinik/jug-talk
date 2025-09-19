package com.nexi.iso8583.extension.deployment;

import com.nexi.iso8583.extension.runtime.ISOCompositeField;
import com.nexi.iso8583.extension.runtime.ISOSubField;
import com.nexi.iso8583.extension.runtime.messages.AuthorizationRequest;
import com.nexi.iso8583.extension.runtime.ISO8583Version;
import com.nexi.iso8583.extension.runtime.ISOField;
import com.nexi.iso8583.extension.runtime.LuhnCheckDigit;
import com.nexi.iso8583.extension.runtime.MessageOriginDomain;
import com.nexi.iso8583.extension.runtime.pos.Ecommerce;
import com.nexi.iso8583.extension.runtime.pos.Moto;
import com.nexi.iso8583.extension.runtime.processing.AccountVerification;
import com.nexi.iso8583.extension.runtime.processing.Purchase;
import com.nexi.iso8583.extension.runtime.processing.Refund;
import org.jboss.jandex.DotName;

public final class ISO8583DotNames {
    public static final DotName ISO8583_VERSION_DOT_NAME = DotName.createSimple(ISO8583Version.class.getName());
    public static final DotName MESSAGE_ORIGIN_DOT_NAME = DotName.createSimple(MessageOriginDomain.class.getName());
    public static final DotName AUTHORIZATION_REQUEST_DOT_NAME = DotName.createSimple(AuthorizationRequest.class.getName());
    public static final DotName PURCHASE_DOT_NAME = DotName.createSimple(Purchase.class.getName());
    public static final DotName MOTO_DOT_NAME = DotName.createSimple(Moto.class.getName());
    public static final DotName ECOMMERCE_DOT_NAME = DotName.createSimple(Ecommerce.class.getName());
    public static final DotName REFUND_DOT_NAME = DotName.createSimple(Refund.class.getName());
    public static final DotName ACCOUNT_VERIFICATION_DOT_NAME = DotName.createSimple(AccountVerification.class.getName());
    public static final DotName ISO_FIELD_DOT_NAME = DotName.createSimple(ISOField.class.getName());
    public static final DotName ISO_SUB_FIELD_DOT_NAME = DotName.createSimple(ISOSubField.class.getName());
    public static final DotName COMPOSITE_ISO_FIELD_DOT_NAME = DotName.createSimple(ISOCompositeField.class.getName());
    public static final DotName LUHN_CKECK_DIGIT_DOT_NAME = DotName.createSimple(LuhnCheckDigit.class.getName());
}
