package com.redhat.demo.rules;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNTest {
    private static final Logger LOG = LoggerFactory.getLogger(DMNTest.class);
    @Test
    public void testDMN() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        DMNRuntime runtime = KieRuntimeFactory.of(kieContainer.getKieBase("demo-kjar")).get(DMNRuntime.class);
        assertThat(runtime).isNotNull();

        DMNModel model = runtime.getModel("ns2", "RoutingEventKJAR");
        assertThat(model).isNotNull();
        assertThat(model.getMessages(DMNMessage.Severity.ERROR)).hasSize(0);
        
        DMNContext context = runtime.newContext();
        context.set("event", routingEvent("MMS", "ADT", "A03"));

        DMNResult result = runtime.evaluateAll(model, context);
        LOG.info("{}", result);
        assertThat(result.getMessages(DMNMessage.Severity.ERROR)).hasSize(0);
        assertThat(result.getDecisionResultByName("topic names").getResult()).asList().hasSize(2).containsExactly("MMSAllADT", "MMSDischarges");
    }

    private static Map<String, Object> routingEvent(String sendingApp, String messageType, String messageEvent) {
        Map<String, Object> result = new HashMap<>();
        result.put("SendingApp", sendingApp);
        result.put("MessageType", messageType);
        result.put("MessageEvent", messageEvent);
        return result;
    }
}
