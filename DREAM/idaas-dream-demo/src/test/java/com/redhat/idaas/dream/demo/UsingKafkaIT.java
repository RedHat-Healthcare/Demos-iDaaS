package com.redhat.idaas.dream.demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.redhat.idaas.dream.demo.route.IDAASEmbeddedRulesRouteBuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class UsingKafkaIT extends CamelTestSupport {

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Test
    public void test() throws Exception {
        final String hl7msg1 = hl7msg("MMS", "ADT", "A03");
        final String hl7msg2 = hl7msg("MMS", "ADT", "A01");
        template.requestBody("direct:hl7", hl7msg1);
        template.requestBody("direct:hl7", hl7msg2);

        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID());
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig,
                new StringDeserializer(), new StringDeserializer())) {
            final ArrayList<String> events = new ArrayList<>();

            consumer.subscribe(Collections.singletonList("MMSAllADT"));
            Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (final ConsumerRecord<String, String> record : records) {
                    events.add(record.value());
                }
                Assertions.assertThat(events).hasSize(2).containsExactly(hl7msg1, hl7msg2);
            });

            consumer.unsubscribe();
            events.clear();

            consumer.subscribe(Collections.singletonList("MMSDischarges"));
            Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (final ConsumerRecord<String, String> record : records) {
                    events.add(record.value());
                }
                Assertions.assertThat(events).hasSize(1).containsExactly(hl7msg1);
            });
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new IDAASEmbeddedRulesRouteBuilder();
    }

    private String hl7msg(String sendingApp, String p1, String p2) {
        return "MSH|^~\\&|" + sendingApp + "|DH|LABADT|DH|201301011226||" + p1 + "^" + p2
                + "|HL7MSG00001|P|2.3|\nEVN|A01|201301011223||\nPID|||MRN12345^5^M11||APPLESEED^JOHN^A^III||19710101|M||C|1 DATICA STREET^^MADISON^WI^53005-1020|GL|(414)379-1212|(414)271-3434||S||MRN12345001^2^M10|123456789|987654^NC|\nNK1|1|APPLESEED^BARBARA^J|WIFE||||||NK^NEXT OF KIN\nPV1|1|I|2000^2012^01||||004777^GOOD^SIDNEY^J.|||SUR||||ADM|A0|";
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties p = new Properties();
        p.setProperty("idaas.kafka.bootstrap", kafka.getBootstrapServers());
        return p;
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();

        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession ksession = kieContainer.getKieBase("demo-embedded").newKieSession();
        registry.bind("ksession1", ksession);

        return registry;
    }
}