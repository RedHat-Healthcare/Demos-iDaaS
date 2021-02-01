package com.redhat.idaas.dream.demo.route;

import org.apache.camel.builder.RouteBuilder;

public class KafkaConsumerRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("kafka:MMSAllADT,MMSDischarges?brokers={{idaas.kafka.bootstrap}}")
            .log("Message received from Kafka: topic ${headers[kafka.TOPIC]}, partition ${headers[kafka.PARTITION]} offset ${headers[kafka.OFFSET]} key ${headers[kafka.KEY]}\n---\n${body}\n---\n");
    }

}
