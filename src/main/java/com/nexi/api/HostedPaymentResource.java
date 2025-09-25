package com.nexi.api;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//@Path("/hpp")
public class HostedPaymentResource {

    @Inject
    @Location("hosted-payment-page.html")
    Template hostedPaymentPage;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance hpp() {
        return hostedPaymentPage.instance();
    }
}
