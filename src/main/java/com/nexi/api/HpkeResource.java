package com.nexi.api;

import com.nexi.hpke.SenderBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

//@Path("/hpke")
public class HpkeResource {

    private final SenderBean senderBean;

    @Inject
    public HpkeResource(SenderBean senderBean) {
        this.senderBean = senderBean;
    }

    @POST
    public Response postHpke() throws Exception {
        senderBean.send("Hello Lorenzo!");
        return Response.ok().build();
    }
}
