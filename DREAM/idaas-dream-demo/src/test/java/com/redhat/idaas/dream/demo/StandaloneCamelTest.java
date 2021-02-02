package com.redhat.idaas.dream.demo;

import static com.redhat.idaas.dream.demo.TestUtils.routingEvent;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ClaimCheckOperation;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.camel.embedded.dmn.DecisionsToHeadersProcessor;
import org.kie.camel.embedded.dmn.ToDMNEvaluateAllCommandProcessor;
import org.kie.camel.embedded.dmn.ToMapProcessor;

import io.connectedhealth_idaas.eventbuilder.events.platform.RoutingEvent;

public class StandaloneCamelTest extends CamelTestSupport {
    protected Context jndiContext;

    @Override
    protected Context createJndiContext() throws Exception {
        jndiContext = super.createJndiContext();
        configureDroolsContext(jndiContext);
        return jndiContext;
    }

    protected void configureDroolsContext(Context jndiContext) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession ksession = kieContainer.getKieBase("demo-embedded").newKieSession();

        try {
            jndiContext.bind("ksession1", ksession);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        final Processor toMap = new ToMapProcessor("event");
        final Processor toDMNCommand = new ToDMNEvaluateAllCommandProcessor("ns1", "RoutingEvent", "dmnResult");
        final Processor dmnToHeader = DecisionsToHeadersProcessor.builder("dmnResult", "topicsHeader", "topic names")
                                                                 .build();
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:start").claimCheck(ClaimCheckOperation.Push)
                                    .process(toMap)
                                    .process(toDMNCommand)
                                    .to("kie-local://ksession1?channel=default") // Drools+DMN processing of iDAAS object
                                    .process(dmnToHeader)
                                    .claimCheck(ClaimCheckOperation.Pop)
                                    .to("log:com.redhat.idaas?level=INFO&showAll=true&multiline=true")
                                    .filter(simple("${header.topicsHeader} contains 'MMSAllADT'"))
                                        .to("mock:MMSAllADT")
                                    .end()
                                    .filter(simple("${header.topicsHeader} contains 'MMSDischarges'"))
                                        .to("mock:MMSDischarges")
                                    .end();
            }
        };
    }
    
    @Test
    public void test() throws Exception {
        RoutingEvent adt1 = routingEvent("MMS", "ADT", null);
        RoutingEvent adt2 = routingEvent("MMS", "ADT", null);
        RoutingEvent discharge1 = routingEvent("MMS", "ADT", "A03");
        RoutingEvent mms = routingEvent("MMS", null, null);

        MockEndpoint mockMMSAllADT = getMockEndpoint("mock:MMSAllADT");
        mockMMSAllADT.expectedMessageCount(3);
        mockMMSAllADT.expectedBodiesReceived(adt1, adt2, discharge1);
        MockEndpoint mockMMSDischarges = getMockEndpoint("mock:MMSDischarges");
        mockMMSDischarges.expectedMessageCount(1);
        mockMMSDischarges.expectedBodiesReceived(discharge1);

        template.requestBody("direct:start", adt1);
        template.requestBody("direct:start", adt2);
        template.requestBody("direct:start", discharge1);
        template.requestBody("direct:start", mms);

        mockMMSAllADT.assertIsSatisfied();
        mockMMSDischarges.assertIsSatisfied();
    }
}