package com.nexi.apm;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://api-m.sandbox.paypal.com")
public interface PayPalRestClient {

    @POST
    @Path("/v1/oauth2/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    Response getToken(@HeaderParam("Authorization") String authorization, @FormParam("grant_type") String grantType);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/v2/checkout/orders")
    Response createOrder(@HeaderParam("Authorization") String authorization, OrderRequest orderRequest);

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class OrderRequest {
        private String intent;
        private PaymentSource paymentSource;
        private PurchaseUnit[] purchaseUnits;

        // Getters and setters

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public PaymentSource getPaymentSource() {
            return paymentSource;
        }

        public void setPaymentSource(PaymentSource paymentSource) {
            this.paymentSource = paymentSource;
        }

        public PurchaseUnit[] getPurchaseUnits() {
            return purchaseUnits;
        }

        public void setPurchaseUnits(PurchaseUnit[] purchaseUnits) {
            this.purchaseUnits = purchaseUnits;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class PaymentSource {
        private PayPal paypal;

        // Getters and setters

        public PayPal getPaypal() {
            return paypal;
        }

        public void setPaypal(PayPal paypal) {
            this.paypal = paypal;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class PayPal {
        private ExperienceContext experienceContext;

        // Getters and setters

        public ExperienceContext getExperienceContext() {
            return experienceContext;
        }

        public void setExperienceContext(ExperienceContext experienceContext) {
            this.experienceContext = experienceContext;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class ExperienceContext {
        private String paymentMethodPreference;
        private String landingPage;
        private String shippingPreference;
        private String userAction;
        private String returnUrl;
        private String cancelUrl;

        // Getters and setters

        public String getPaymentMethodPreference() {
            return paymentMethodPreference;
        }

        public void setPaymentMethodPreference(String paymentMethodPreference) {
            this.paymentMethodPreference = paymentMethodPreference;
        }

        public String getLandingPage() {
            return landingPage;
        }

        public void setLandingPage(String landingPage) {
            this.landingPage = landingPage;
        }

        public String getShippingPreference() {
            return shippingPreference;
        }

        public void setShippingPreference(String shippingPreference) {
            this.shippingPreference = shippingPreference;
        }

        public String getUserAction() {
            return userAction;
        }

        public void setUserAction(String userAction) {
            this.userAction = userAction;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getCancelUrl() {
            return cancelUrl;
        }

        public void setCancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class PurchaseUnit {
        private String invoiceId;
        private Amount amount;
        private Item[] items;

        // Getters and setters

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public Amount getAmount() {
            return amount;
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Item[] getItems() {
            return items;
        }

        public void setItems(Item[] items) {
            this.items = items;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class Amount {
        private String currencyCode;
        private String value;
        private Breakdown breakdown;

        // Getters and setters

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Breakdown getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(Breakdown breakdown) {
            this.breakdown = breakdown;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class Breakdown {
        private ItemTotal itemTotal;
        private Shipping shipping;

        // Getters and setters

        public ItemTotal getItemTotal() {
            return itemTotal;
        }

        public void setItemTotal(ItemTotal itemTotal) {
            this.itemTotal = itemTotal;
        }

        public Shipping getShipping() {
            return shipping;
        }

        public void setShipping(Shipping shipping) {
            this.shipping = shipping;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class ItemTotal {
        private String currencyCode;
        private String value;

        // Getters and setters

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class Shipping {
        private String currencyCode;
        private String value;

        // Getters and setters

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class Item {
        private String name;
        private String description;
        private UnitAmount unitAmount;
        private String quantity;
        private String category;
        private String sku;
        private String imageUrl;
        private String url;
        private Upc upc;

        // Getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public UnitAmount getUnitAmount() {
            return unitAmount;
        }

        public void setUnitAmount(UnitAmount unitAmount) {
            this.unitAmount = unitAmount;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Upc getUpc() {
            return upc;
        }

        public void setUpc(Upc upc) {
            this.upc = upc;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class UnitAmount {
        private String currencyCode;
        private String value;

        // Getters and setters

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    class Upc {
        private String type;
        private String code;

        // Getters and setters

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
