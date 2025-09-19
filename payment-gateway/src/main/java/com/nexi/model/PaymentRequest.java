package com.nexi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class PaymentRequest {

    @Schema(description = "Primary Account Number", example = "4205260000000005", required = true)
    @NotBlank(message="PAN may not be blank")
    @Size(min = 16, max = 19, message = "The PAN length must be between 16 and 19 digits")
    private String pan;

    @Schema(description = "Expiration date in MMYY format", example = "1231", required = true)
    @NotBlank(message="Expiry may not be blank")
    @Size(min = 4, max = 4, message = "The Expiry length must be 4 digits")
    private String expiry;

    @Schema(description = "Security code CVV", example = "123", required = true)
    @Size(min = 3, max = 4, message = "The CVV length must be between 3 and 4 digits")
    private String cvv;

    @Schema(
            description = "Transaction amount in cents (smallest unit of currency, e.g., 100 means 1.00 EUR)",
            example = "100",
            required = true
    )
    @Min(message="Amount must be greater than 0", value = 1)
    private Integer amount;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
