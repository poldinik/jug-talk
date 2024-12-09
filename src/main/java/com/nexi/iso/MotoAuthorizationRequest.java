package com.nexi.iso;


import com.nexi.iso8583.extension.runtime.ISO8583Version;
import com.nexi.iso8583.extension.runtime.ISOCompositeField;
import com.nexi.iso8583.extension.runtime.ISOField;
import com.nexi.iso8583.extension.runtime.ISOSubField;
import com.nexi.iso8583.extension.runtime.LuhnCheckDigit;
import com.nexi.iso8583.extension.runtime.MessageOriginDomain;
import com.nexi.iso8583.extension.runtime.messages.AuthorizationRequest;
import com.nexi.iso8583.extension.runtime.pos.Moto;
import com.nexi.iso8583.extension.runtime.processing.Purchase;

import static com.nexi.iso8583.extension.runtime.MessageOriginDomainType.ACQUIRER;

@ISO8583Version(version = "1")
@MessageOriginDomain(ACQUIRER)
@AuthorizationRequest
@Purchase
@Moto
public class MotoAuthorizationRequest {

    @ISOField(fieldNumber = 2)
    @LuhnCheckDigit
    private String pan;

    @ISOField(fieldNumber = 4)
    private String amount;

    @ISOField(fieldNumber = 11)
    private String stan;

    @ISOField(fieldNumber = 12)
    private String localTransactionDateTime;

    @ISOField(fieldNumber = 14)
    private String expirationDate;

    @ISOField(fieldNumber = 22)
    private String posDataCode;

    @ISOField(fieldNumber = 24)
    private String functionCode;

    @ISOField(fieldNumber = 25)
    private String messageReasonCode;

    @ISOField(fieldNumber = 26)
    private String merchantCategoryCode;

    @ISOField(fieldNumber = 37)
    private String retrievalReferenceNumber;

    public MotoAuthorizationRequest(MotoAuthorizationRequestBuilder builder) {
        this.pan = builder.pan;
        this.amount = builder.amount;
        this.stan = builder.stan;
        this.localTransactionDateTime = builder.localTransactionDateTime;
        this.expirationDate = builder.expirationDate;
        this.posDataCode = builder.posDataCode;
        this.functionCode = builder.functionCode;
        this.messageReasonCode = builder.messageReasonCode;
        this.merchantCategoryCode = builder.merchantCategoryCode;
        this.retrievalReferenceNumber = builder.retrievalReferenceNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getLocalTransactionDateTime() {
        return localTransactionDateTime;
    }

    public void setLocalTransactionDateTime(String localTransactionDateTime) {
        this.localTransactionDateTime = localTransactionDateTime;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPosDataCode() {
        return posDataCode;
    }

    public void setPosDataCode(String posDataCode) {
        this.posDataCode = posDataCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getMessageReasonCode() {
        return messageReasonCode;
    }

    public void setMessageReasonCode(String messageReasonCode) {
        this.messageReasonCode = messageReasonCode;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public static class MotoAuthorizationRequestBuilder {

        private String pan;
        private String amount;
        private String stan;
        private String localTransactionDateTime;
        private String expirationDate;
        private String posDataCode;
        private String functionCode;
        private String messageReasonCode;
        private String merchantCategoryCode;
        private String retrievalReferenceNumber;

        public MotoAuthorizationRequestBuilder pan(String pan) {
            this.pan = pan;
            return this;
        }

        public MotoAuthorizationRequestBuilder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public MotoAuthorizationRequestBuilder stan(String stan) {
            this.stan = stan;
            return this;
        }

        public MotoAuthorizationRequestBuilder localTransactionDateTime(String localTransactionDateTime) {
            this.localTransactionDateTime = localTransactionDateTime;
            return this;
        }

        public MotoAuthorizationRequestBuilder expirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public MotoAuthorizationRequestBuilder posDataCode(String posDataCode) {
            this.posDataCode = posDataCode;
            return this;
        }

        public MotoAuthorizationRequestBuilder functionCode(String functionCode) {
            this.functionCode = functionCode;
            return this;
        }

        public MotoAuthorizationRequestBuilder messageReasonCode(String messageReasonCode) {
            this.messageReasonCode = messageReasonCode;
            return this;
        }

        public MotoAuthorizationRequestBuilder merchantCategoryCode(String merchantCategoryCode) {
            this.merchantCategoryCode = merchantCategoryCode;
            return this;
        }

        public MotoAuthorizationRequestBuilder retrievalReferenceNumber(String retrievalReferenceNumber) {
            this.retrievalReferenceNumber = retrievalReferenceNumber;
            return this;
        }

        public MotoAuthorizationRequest build() {
            return new MotoAuthorizationRequest(this);
        }

    }

    public static MotoAuthorizationRequestBuilder builder() {
        return new MotoAuthorizationRequestBuilder();
    }
}
