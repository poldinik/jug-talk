package com.nexi.api;

import com.nexi.iso.MotoAuthorizationRequest;
import com.nexi.iso8583.extension.runtime.ISOSerializer;
import com.nexi.model.OperationResponse;
import com.nexi.model.PaymentRequest;
import com.nexi.services.AuthorizationGateway;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Path("/payments")
public class PaymentResource {

    public static final String ISO_DATE_TIME_FORMAT = "yyMMddHHmmss";
    private final AuthorizationGateway authorizationGateway;

    @Inject
    public PaymentResource(AuthorizationGateway authorizationGateway) {
        this.authorizationGateway = authorizationGateway;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Process MO.TO Authorization Request",
            description = "Executes a Mail Order/Telephone Order (MO.TO) authorization request using provided payment details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Authorization successful",
                    content = @Content(schema = @Schema(implementation = OperationResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid request payload"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public Response moto(@Valid PaymentRequest paymentRequest) {
        authorizationGateway.pay(
                MotoAuthorizationRequest.builder()
                        .pan(paymentRequest.getPan())
                        .amount(paymentRequest.getAmount().toString())
                        .stan("12345")
                        .localTransactionDateTime(
                                OffsetDateTime
                                        .now(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT))
                        )
                        .expirationDate(paymentRequest.getExpiry())
                        .functionCode("100")
                        .messageReasonCode("1510")
                        .merchantCategoryCode("123")
                        .retrievalReferenceNumber(ISOSerializer.rrnGenerate())
                        .build()
        );
        return Response.ok(new OperationResponse(LocalDateTime.now(), "OK")).build();
    }
}