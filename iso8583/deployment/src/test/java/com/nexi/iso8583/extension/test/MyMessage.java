package com.nexi.iso8583.extension.test;


import com.nexi.iso8583.extension.runtime.ISO8583Version;
import com.nexi.iso8583.extension.runtime.ISOCompositeField;
import com.nexi.iso8583.extension.runtime.ISOField;
import com.nexi.iso8583.extension.runtime.ISOSubField;
import com.nexi.iso8583.extension.runtime.LuhnCheckDigit;
import com.nexi.iso8583.extension.runtime.MessageOriginDomain;
import com.nexi.iso8583.extension.runtime.messages.AuthorizationRequest;
import com.nexi.iso8583.extension.runtime.processing.Purchase;
import com.nexi.iso8583.extension.runtime.pos.Moto;

import static com.nexi.iso8583.extension.runtime.MessageOriginDomainType.ACQUIRER;

@ISO8583Version(version = "1")
@MessageOriginDomain(ACQUIRER)
@AuthorizationRequest
@Purchase
@Moto
public class MyMessage {

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

    @ISOField(fieldNumber = 15)
    private String settlementDate;

    @ISOField(fieldNumber = 19)
    private String acquiringInstitutionCountryCode;

    @ISOField(fieldNumber = 22)
    private String posDataCode;

    @ISOField(fieldNumber = 24)
    private String functionCode;

    @ISOField(fieldNumber = 25)
    private String messageReasonCode;

    @ISOField(fieldNumber = 26)
    private String merchantCategoryCode;

    @ISOField(fieldNumber = 28)
    private String reconciliationDate;

    @ISOField(fieldNumber = 31)
    private String acquirerReferenceData;

    @ISOField(fieldNumber = 37)
    private String retrievalReferenceNumber;

    @ISOField(fieldNumber = 47)
    private String additionalNationalData;

    @ISOField(fieldNumber = 119)
    @ISOCompositeField
    private DigitalPaymentData digitalPaymentData;

    public static class DigitalPaymentData {

        @ISOSubField(fieldNumber = 1)
        private byte[] dsrpCryptogram;

        @ISOSubField(fieldNumber = 21)
        private byte[] tavvCryptogram;

        public byte[] getDsrpCryptogram() {
            return dsrpCryptogram;
        }

        public void setDsrpCryptogram(byte[] dsrpCryptogram) {
            this.dsrpCryptogram = dsrpCryptogram;
        }

        public byte[] getTavvCryptogram() {
            return tavvCryptogram;
        }

        public void setTavvCryptogram(byte[] tavvCryptogram) {
            this.tavvCryptogram = tavvCryptogram;
        }
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

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
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

    public String getReconciliationDate() {
        return reconciliationDate;
    }

    public void setReconciliationDate(String reconciliationDate) {
        this.reconciliationDate = reconciliationDate;
    }

    public String getAcquirerReferenceData() {
        return acquirerReferenceData;
    }

    public void setAcquirerReferenceData(String acquirerReferenceData) {
        this.acquirerReferenceData = acquirerReferenceData;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public DigitalPaymentData getDigitalPaymentData() {
        return digitalPaymentData;
    }

    public void setDigitalPaymentData(DigitalPaymentData digitalPaymentData) {
        this.digitalPaymentData = digitalPaymentData;
    }

    public String getAcquiringInstitutionCountryCode() {
        return acquiringInstitutionCountryCode;
    }

    public void setAcquiringInstitutionCountryCode(String acquiringInstitutionCountryCode) {
        this.acquiringInstitutionCountryCode = acquiringInstitutionCountryCode;
    }

    public String getAdditionalNationalData() {
        return additionalNationalData;
    }

    public void setAdditionalNationalData(String additionalNationalData) {
        this.additionalNationalData = additionalNationalData;
    }
}
