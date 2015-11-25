package com.miwa.mtt.sender.impl;

import com.miwa.mtt.pojo.Stub;
import com.miwa.mtt.sender.Sender;
import com.miwa.mtt.util.ApplicationProperties;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
public class SynchSender implements Sender {
    private Stub stub;

    public SynchSender(Stub stub) {
        this.stub = stub;
    }

    public void Send() {
        System.out.println("Sending stub" + stub.getName());

        Client client = Client.create();
        ApplicationProperties properties = ApplicationProperties.getInstance();
        String url = properties.getProperty("synch.url") + ":" + properties.getProperty("synch.port") + stub.getType().getEndpoint();

        // Getting the right mediatype
        String mediatype = stub.getMessage().getMediatype();
        mediatype = mediatype == null ? properties.getProperty("synch.default.mediatype") : mediatype;

        String method = stub.getMessage().getMethod();
        method = method == null ? properties.getProperty("synch.default.method") : method;

        WebResource webResource = client.resource(url);
        ClientResponse response;
        switch (method) {
            case "PUT":
                response = webResource.type(mediatype).put(ClientResponse.class, stub.getMessage().getValue());
                break;
            case "POST":
                response = webResource.type(mediatype).post(ClientResponse.class, stub.getMessage().getValue());
                break;
            default:
                throw new IllegalStateException();
        }

        if (response.getStatus() != 201) {
            // Si on ne me renvoie pas 201 il y a eu un soucis
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

    }
}
