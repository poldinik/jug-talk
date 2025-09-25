package com.nexi.api;

import com.nexi.apm.PayPalRestClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.util.Map;

//@Path("/paypal")
public class PayPalResource {

    @Inject
    @RestClient
    PayPalRestClient payPalRestClient;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response post() {
        // Create the order request
        PayPalRestClient.OrderRequest orderRequest = new PayPalRestClient.OrderRequest();
        orderRequest.setIntent("AUTHORIZE");
        // Set up the PaymentSource (PayPal)
        PayPalRestClient.PaymentSource paymentSource = new PayPalRestClient.PaymentSource();
        PayPalRestClient.PayPal paypal = new PayPalRestClient.PayPal();

        paymentSource.setPaypal(paypal);
        orderRequest.setPaymentSource(paymentSource);

// Create the purchase unit
        PayPalRestClient.PurchaseUnit purchaseUnit = new PayPalRestClient.PurchaseUnit();
        purchaseUnit.setInvoiceId("INV123456");

// Define the amount
        PayPalRestClient.Amount amount = new PayPalRestClient.Amount();
        amount.setCurrencyCode("EUR");
        amount.setValue("70.00");
        PayPalRestClient.Breakdown breakdown = new PayPalRestClient.Breakdown();
        PayPalRestClient.ItemTotal itemTotal = new PayPalRestClient.ItemTotal();
        itemTotal.setCurrencyCode("EUR");
        itemTotal.setValue("70.00");
        breakdown.setItemTotal(itemTotal);
        amount.setBreakdown(breakdown);

// Set the amount
        purchaseUnit.setAmount(amount);

// Optionally, define the items in the purchase unit
        PayPalRestClient.Item[] items = new PayPalRestClient.Item[1];  // Define an array of items for the purchase unit

        PayPalRestClient.Item item = new PayPalRestClient.Item();
        item.setName("Pantaloni Dondup");
        item.setQuantity("1");
        item.setCategory("PHYSICAL_GOODS");
        PayPalRestClient.UnitAmount unitAmount = new PayPalRestClient.UnitAmount();
        unitAmount.setCurrencyCode("EUR");
        unitAmount.setValue("70.00");
        item.setUnitAmount(unitAmount);
        items[0] = item;

        purchaseUnit.setItems(items);

// Set the purchase units in the order request
        orderRequest.setPurchaseUnits(new PayPalRestClient.PurchaseUnit[]{ purchaseUnit });
        Response tokenResponse = payPalRestClient.getToken("Basic <replace-me>", "client_credentials");
        return payPalRestClient.createOrder("Bearer " + tokenResponse.readEntity(Map.class).get("access_token"), orderRequest);
    }

    @Provider
    public static class BadRequestExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {


        @Override
        public Response toResponse(ClientWebApplicationException exception) {
            return exception.getResponse();
        }
    }
}
