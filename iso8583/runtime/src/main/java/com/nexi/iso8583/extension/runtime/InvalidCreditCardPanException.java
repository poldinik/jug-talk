package com.nexi.iso8583.extension.runtime;

public class InvalidCreditCardPanException extends Exception {
    public InvalidCreditCardPanException() {
        super("The provided credit card PAN is invalid.");
    }

    public InvalidCreditCardPanException(String message) {
        super(message);
    }
}
