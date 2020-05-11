package com.log.analytics.api.v1;

import com.log.analytics.com.log.analytics.entity.Log;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/laa")
public class IngestService {

    private static List<Log> logs = new ArrayList<Log>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/health")
    public String healthCheck() {

        return "";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metrics")
    public List<Contact> getMetrics() {
        return contacts;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String ingest(String json) {

        if (contact.getName() == null || contact.getName().trim().equals("")) {
            throw new WebApplicationException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("O nome do contato é obrigatório").build());
        }

        contacts.add(contact);
        contact.setId(contacts.indexOf(contact) + 1);
        return contact;
    }

}
