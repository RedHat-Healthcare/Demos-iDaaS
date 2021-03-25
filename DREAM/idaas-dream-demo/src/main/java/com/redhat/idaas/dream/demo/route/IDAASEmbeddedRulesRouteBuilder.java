package com.redhat.idaas.dream.demo.route;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.ClaimCheckOperation;
import org.kie.camel.embedded.dmn.DecisionsToHeadersProcessor;
import org.kie.camel.embedded.dmn.ToDMNEvaluateAllCommandProcessor;
import org.kie.camel.embedded.dmn.ToMapProcessor;

import io.connectedhealth_idaas.eventbuilder.parsers.clinical.HL7RoutingEventParser;

public class IDAASEmbeddedRulesRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        final Processor toMap = new ToMapProcessor("event");
        final Processor toDMNCommand = new ToDMNEvaluateAllCommandProcessor("ns1", "RoutingEvent", "dmnResult");
        final Processor dmnToHeader = DecisionsToHeadersProcessor.builder("dmnResult", "topicsHeader", "topic names")
                                                                 .build();

        from("direct:hl7")
            .bean(HL7RoutingEventParser.class, "buildRoutingEvent(${body})")
            .claimCheck(ClaimCheckOperation.Push)
                .process(toMap)
                .process(toDMNCommand)
                .to("kie-local://ksession1?channel=default") // Drools+DMN processing of iDAAS object
                .process(dmnToHeader)
            .claimCheck(ClaimCheckOperation.Pop)
            .to("log:com.redhat.idaas?level=DEBUG&showAll=true&multiline=true")
            .transform().simple("${body.messageData}")
            .choice()
            .when(simple("${header.topicsHeader.size} > 0"))
                .loop(simple("${header.topicsHeader.size}"))
                    .setHeader(KafkaConstants.TOPIC, 
                        simple("${header.topicsHeader[${exchangeProperty.CamelLoopIndex}]}"))
                    .to("kafka:default?brokers={{idaas.kafka.bootstrap}}")
                .endChoice()
            .otherwise()
                .to("kafka:CATCH_ALL?brokers={{idaas.kafka.bootstrap}}")
            ;
    }
}