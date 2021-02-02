package com.redhat.idaas.dream.demo.route;

import org.apache.camel.builder.RouteBuilder;

public class RESTRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().port(8080);

        rest("/hl7")
            .post("/new").consumes("text/plain").to("direct:rest-hl7");

        from("direct:rest-hl7")
            .to("direct:hl7")
            .transform().constant("OK.");
    }
}