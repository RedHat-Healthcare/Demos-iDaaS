package com.redhat.idaas.dream.demo;

import com.redhat.idaas.dream.demo.route.IDAASEmbeddedRulesRouteBuilder;
import com.redhat.idaas.dream.demo.route.KafkaConsumerRouteBuilder;
import com.redhat.idaas.dream.demo.route.RESTRouteBuilder;

import org.apache.camel.main.Main;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DemoCamelEmbeddedRulesMain {
    private Main main;

    public static void main(String[] args) throws Exception {
        DemoCamelEmbeddedRulesMain m = new DemoCamelEmbeddedRulesMain();
        m.boot();
    }

    public void boot() throws Exception {
        main = new Main();
        main.setPropertyPlaceholderLocations("application.properties");

        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession ksession = kieContainer.getKieBase("demo-embedded").newKieSession();
        main.bind("ksession1", ksession);
        
        main.addRouteBuilder(new IDAASEmbeddedRulesRouteBuilder());
        main.addRouteBuilder(new RESTRouteBuilder());
        main.addRouteBuilder(new KafkaConsumerRouteBuilder());

        System.out.println("Starting Camel. Use CTRL+C to terminate.\n");
        main.run();
    }
}
