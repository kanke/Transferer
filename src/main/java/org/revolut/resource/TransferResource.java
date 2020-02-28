package org.revolut.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
public class TransferResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds() {
        return Response.ok().build();
    }
}
