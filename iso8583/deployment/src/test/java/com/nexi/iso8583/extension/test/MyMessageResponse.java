package com.nexi.iso8583.extension.test;

import com.nexi.iso8583.extension.runtime.ISO8583Version;
import com.nexi.iso8583.extension.runtime.ISOField;
import com.nexi.iso8583.extension.runtime.MessageOriginDomain;
import com.nexi.iso8583.extension.runtime.messages.AuthorizationRequestResponse;

import static com.nexi.iso8583.extension.runtime.MessageOriginDomainType.ACQUIRER;

@ISO8583Version(version = "1")
@MessageOriginDomain(ACQUIRER)
@AuthorizationRequestResponse
public class MyMessageResponse {

    @ISOField(fieldNumber = 2)
    private String pan;

    @ISOField(fieldNumber = 3)
    private String processingCode;

    @ISOField(fieldNumber = 4)
    private String amount;

    @ISOField(fieldNumber = 11)
    private String stan;

    @ISOField(fieldNumber = 12)
    private String dateTime;

    @ISOField(fieldNumber = 14)
    private String expiration;

    @ISOField(fieldNumber = 22)
    private String posDataCode;

    @ISOField(fieldNumber = 24)
    private String functionCode;

    @ISOField(fieldNumber = 25)
    private String messageReasonCode;

    @ISOField(fieldNumber = 26)
    private String merchantCategoryCOde;



}
