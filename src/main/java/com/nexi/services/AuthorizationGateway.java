package com.nexi.services;

import com.nexi.iso.MotoAuthorizationRequest;
import com.nexi.iso8583.extension.runtime.ISOSerializer;
import com.nexi.utils.IsoPrinter;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jpos.iso.packager.ISO93APackager;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class AuthorizationGateway {

    private final ISOSerializer isoSerializer;

    @Inject
    public AuthorizationGateway(ISOSerializer isoSerializer) {
        this.isoSerializer = isoSerializer;
    }

    public void pay(MotoAuthorizationRequest motoAuthorizationRequest) {
        byte[] packed = isoSerializer.serialize(motoAuthorizationRequest, new ISO93APackager());
        String msg = IsoPrinter.printMsg(packed);
        Log.info("Sent Authorization Request: \n\n" + new String(packed, StandardCharsets.UTF_8)  + "\n" + msg);
    }
}
