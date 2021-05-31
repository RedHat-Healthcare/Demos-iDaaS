/*
 * Copyright 2019 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package com.redhat.idaas.connect.fhir;

import ca.uhn.fhir.store.IAuditDataStore;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.MultipleConsumersSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaEndpoint;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sun.util.calendar.BaseCalendar;
import java.time.LocalDate;
// Add Imports for iDaaS Event Builder
import io.connectedhealth_idaas.eventbuilder.parsers.clinical.FHIRStreamParser;

@Component
public class CamelConfiguration extends RouteBuilder {
  private static final Logger log = LoggerFactory.getLogger(CamelConfiguration.class);

  @Autowired
  private ConfigProperties config;

  @Bean
  private KafkaEndpoint kafkaEndpoint(){
    KafkaEndpoint kafkaEndpoint = new KafkaEndpoint();
    return kafkaEndpoint;
  }
  @Bean
  private KafkaComponent kafkaComponent(KafkaEndpoint kafkaEndpoint){
    KafkaComponent kafka = new KafkaComponent();
    return kafka;
  }

  @Bean
  ServletRegistrationBean camelServlet() {
    // use a @Bean to register the Camel servlet which we need to do
    // because we want to use the camel-servlet component for the Camel REST service
    ServletRegistrationBean mapping = new ServletRegistrationBean();
    mapping.setName("CamelServlet");
    mapping.setLoadOnStartup(1);
    mapping.setServlet(new CamelHttpTransportServlet());
    mapping.addUrlMappings("/iDAAS/*");
    return mapping;
  }

  // Bean for iDaaS Event Builder Specifically the FHIRStreamParser
  @Bean
  public FHIRStreamParser fhirStreamParser() {
    return new FHIRStreamParser();
  }

  private String getKafkaTopicUri(String topic) {
    return "kafka:" + topic +
            "?brokers=" +
            config.getKafkaBrokers();
  }

  private String getFHIRServerUri(String fhirResource) {
    String fhirServerVendor = config.getFhirVendor();
    String fhirServerURI = null;
    if (fhirServerVendor.equals("ibm"))
    {
      //.to("jetty:http://localhost:8090/fhir-server/api/v4/AdverseEvents?bridgeEndpoint=true&exchangePattern=InOut")
      fhirServerURI = "jetty:"+config.getIbmURI()+fhirResource+"?bridgeEndpoint=true&exchangePattern=InOut";
    }
    if (fhirServerVendor.equals("hapi"))
    {
      fhirServerURI = "jetty:"+config.getHapiURI()+fhirResource+"?bridgeEndpoint=true&exchangePattern=InOut";
    }
    if (fhirServerVendor.equals("microsoft"))
    {
      fhirServerURI = "jetty:"+config.getMicrosoftURI()+fhirResource+"?bridgeEndpoint=true&exchangePattern=InOut";
    }
    return fhirServerURI;
  }

  /*
   * Kafka implementation based upon https://camel.apache.org/components/latest/kafka-component.html
   *
   */
  @Override
  public void configure() throws Exception {

    /*
     * Audit
     *
     * Direct component within platform to ensure we can centralize logic
     * There are some values we will need to set within every route
     * We are doing this to ensure we dont need to build a series of beans
     * and we keep the processing as lightweight as possible
     *
     */
    from("direct:auditing")
        .routeId("KIC-KnowledgeInsightConformance")
        .setHeader("messageprocesseddate").simple("${date:now:yyyy-MM-dd}")
        .setHeader("messageprocessedtime").simple("${date:now:HH:mm:ss:SSS}")
        .setHeader("processingtype").exchangeProperty("processingtype")
        .setHeader("industrystd").exchangeProperty("industrystd")
        .setHeader("component").exchangeProperty("componentname")
        .setHeader("messagetrigger").exchangeProperty("messagetrigger")
        .setHeader("processname").exchangeProperty("processname")
        .setHeader("auditdetails").exchangeProperty("auditdetails")
        .setHeader("camelID").exchangeProperty("camelID")
        .setHeader("exchangeID").exchangeProperty("exchangeID")
        .setHeader("internalMsgID").exchangeProperty("internalMsgID")
        .setHeader("bodyData").exchangeProperty("bodyData")
        .convertBodyTo(String.class).to(getKafkaTopicUri("opsmgmt_platformtransactions"))
    ;
    /*
    *  Logging
    */
    from("direct:logging")
        .routeId("Logging")
        .log(LoggingLevel.INFO, log, "Body of Data Being Processed: [${body}]")
        //To invoke Logging
        //.to("direct:logging")
    ;

    /*
     *  Clinical FHIR
     *  ----
     * these will be accessible within the integration when started the default is
     * <hostname>:8080/idaas/<resource>
     *
     */
    from("servlet://allergyintolerance")
        .routeId("FHIRAllergyIntoleranceWithBean")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("messagetrigger").constant("AllergyIntolerance")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Allergy Intolerance message received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_allergyintolerance"))
        // Set Bean Action to change data
        .bean(FHIRStreamParser.class, "buildRoutingEvent(${body})")
        .log(LoggingLevel.INFO, log, "Routing Event: ${body}")
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR-DataTagging")
        .setProperty("messagetrigger").constant("AllergyIntolerance")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Allergy Intolerance message received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("datatagged_allergyintolerance"))
    ;


  }
}