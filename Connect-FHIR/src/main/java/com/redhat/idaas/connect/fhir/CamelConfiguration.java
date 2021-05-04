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
import org.apache.camel.component.fhir.FhirJsonDataFormat;
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

import static org.apache.camel.builder.ProcessorBuilder.setHeader;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.to;

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
    mapping.addUrlMappings("/iDaaS/*");
    return mapping;
  }

  /*
   * Kafka implementation based upon https://camel.apache.org/components/latest/kafka-component.html
   */
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


  @Override
  public void configure() throws Exception {

    /*
     *   HIDN
     *   HIDN - Health information Data Network
     *   Intended to enable simple movement of data aside from specific standards
     *   Common Use Cases are areas to support remote (iOT/Edge) and any other need for small footprints to larger
     *   footprints
     * : Unstructured data, st
     */
    from("direct:hidn")
            .setHeader("messageprocesseddate").simple("${date:now:yyyy-MM-dd}")
            .setHeader("messageprocessedtime").simple("${date:now:HH:mm:ss:SSS}")
            .setHeader("eventdate").simple("eventdate")
            .setHeader("eventtime").simple("eventtime")
            .setHeader("processingtype").exchangeProperty("processingtype")
            .setHeader("industrystd").exchangeProperty("industrystd")
            .setHeader("component").exchangeProperty("componentname")
            .setHeader("processname").exchangeProperty("processname")
            .setHeader("organization").exchangeProperty("organization")
            .setHeader("careentity").exchangeProperty("careentity")
            .setHeader("customattribute1").exchangeProperty("customattribute1")
            .setHeader("customattribute2").exchangeProperty("customattribute2")
            .setHeader("customattribute3").exchangeProperty("customattribute3")
            .setHeader("camelID").exchangeProperty("camelID")
            .setHeader("exchangeID").exchangeProperty("exchangeID")
            .setHeader("internalMsgID").exchangeProperty("internalMsgID")
            .setHeader("bodyData").exchangeProperty("bodyData")
            .setHeader("bodySize").exchangeProperty("bodySize")
            .convertBodyTo(String.class).to(getKafkaTopicUri("hidn"))
    ;

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
        //.convertBodyTo(String.class).to("kafka://localhost:9092?topic=opsmgmt_platformtransactions&brokers=localhost:9092")
        .convertBodyTo(String.class).to(getKafkaTopicUri("opsmgmt_platformtransactions"))
    ;

    /*
    *  Logging
    */
    from("direct:logging")
        .log(LoggingLevel.INFO, log, "FHIR Message: [${body}]")
        //To invoke Logging
        //.to("direct:logging")
    ;

    /*
     *   General iDaaS Platform
     */
    from("servlet://hidn")
            .routeId("HIDN")
            // Data Parsing and Conversions
            // Normal Processing
            .convertBodyTo(String.class)
            .setHeader("messageprocesseddate").simple("${date:now:yyyy-MM-dd}")
            .setHeader("messageprocessedtime").simple("${date:now:HH:mm:ss:SSS}")
            .setHeader("eventdate").simple("eventdate")
            .setHeader("eventtime").simple("eventtime")
            .setHeader("processingtype").exchangeProperty("processingtype")
            .setHeader("industrystd").exchangeProperty("industrystd")
            .setHeader("component").exchangeProperty("componentname")
            .setHeader("processname").exchangeProperty("processname")
            .setHeader("organization").exchangeProperty("organization")
            .setHeader("careentity").exchangeProperty("careentity")
            .setHeader("customattribute1").exchangeProperty("customattribute1")
            .setHeader("customattribute2").exchangeProperty("customattribute2")
            .setHeader("customattribute3").exchangeProperty("customattribute3")
            .setHeader("camelID").exchangeProperty("camelID")
            .setHeader("exchangeID").exchangeProperty("exchangeID")
            .setHeader("internalMsgID").exchangeProperty("internalMsgID")
            .setHeader("bodyData").exchangeProperty("bodyData")
            .setHeader("bodySize").exchangeProperty("bodySize")
            .wireTap("direct:hidn")
    ;

    /*
     *  Binary Resource Upload
     */
    from("servlet://binary")
            .routeId("FHIRBinary")
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("messagetrigger").constant("Binary")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("Binary resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
            // Send To Topic
            .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_binary"))
            // Unmarshal with FHIR
            // need to add the FHIR centric Marshal
            //.unmarshal()
            // Audit UnMarshal Event
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("Binary")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("Binary UnMarshall Activity")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")// Invoke External FHIR Server
            // Send to FHIR Server
            .choice().when(simple("{{idaas.processToFHIR}}"))
                .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
                .to(getFHIRServerUri("Binary"))
                //Process Response
                .convertBodyTo(String.class)
                // set Auditing Properties
                .setProperty("processingtype").constant("data")
                .setProperty("appname").constant("iDAAS-Connect-FHIR")
                .setProperty("industrystd").constant("FHIR")
                .setProperty("messagetrigger").constant("Binary")
                .setProperty("component").simple("${routeId}")
                .setProperty("processname").constant("Response")
                .setProperty("camelID").simple("${camelId}")
                .setProperty("exchangeID").simple("${exchangeId}")
                .setProperty("internalMsgID").simple("${id}")
                .setProperty("bodyData").simple("${body}")
                .setProperty("auditdetails").constant("Binary response resource/bundle received")
                // iDAAS DataHub Processing
                .wireTap("direct:auditing")// Invoke External FHIR Server
            .endChoice()
    ;

    /*
     *  Clinical FHIR
     *  ----
     * these will be accessible within the integration when started the default is
     * <hostname>:8080/idaas/<resource>
     *
     */
    from("servlet://allergyintolerance")
        .routeId("FHIRAllergyIntolerance")
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
        .setProperty("auditdetails").constant("Allergy Intolerance resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_allergyintellorance"))
         // Send to FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("AllergyIntolerance"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("AllergyIntolerance")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("Allergy Intolerance response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;

    from("servlet://condition")
        .routeId("FHIRCondition")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("Condition")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Condition resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_condition"))
        // Send to FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Condition"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("condition")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("condition FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;
    from("servlet://consent")
        .routeId("FHIRConsent")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("Consent")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Consent resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_consent"))
        // Semd to FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Consent"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("consent")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("consent FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;

    // Financial
    from("servlet://account")
        .routeId("FHIRAccount")
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("account")
        .setProperty("component").simple("${routeId}")
        .setProperty("processname").constant("Input")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("auditdetails").constant("account resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_account"))
        // Invoke External FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Account"))
            // Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("account")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Input")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("account FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;

    from("servlet://claim")
        .routeId("FHIRClaim")
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("claim")
        .setProperty("component").simple("${routeId}")
        .setProperty("processname").constant("Input")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("auditdetails").constant("claim resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_claim"))
        // Invoke External FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Claim"))
            // Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("claim")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Input")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("claim FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;

    // Reporting
    from("servlet://measure")
        .routeId("FHIRMeasure")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("Measure")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Measure resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_measure"))
        // Invoke External FHIR Server
       .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Measure"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("measure")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("measure FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;
    from("servlet://measurereport")
        .routeId("FHIRMeasureReport")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("MeasureReport")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Measure Report resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_measurereport"))
        // Invoke External FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("MeasureReport"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("measurereport")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("measurereport FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;

    // Questonaire
    from("servlet://questionnaire")
        .routeId("FHIRQuestionnaire")
        .convertBodyTo(String.class)
        // set Auditing Properties
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-Connect-FHIR")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("questionnaire")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("Questionnaire resource/bundle received")
        // iDAAS DataHub Processing
        .wireTap("direct:auditing")
        // Send To Topic
        .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_questionnaire"))
        // Invoke External FHIR Server
        .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("Questionnaire"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("questionnaire")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("Questionnaire FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
        .endChoice()
    ;
    // Questonaire
    from("servlet://questionnaireresponse")
        .routeId("FHIRQuestionnaireResponse")
        .convertBodyTo(String.class)
       // set Auditing Properties
       .setProperty("processingtype").constant("data")
       .setProperty("appname").constant("iDAAS-Connect-FHIR")
       .setProperty("industrystd").constant("FHIR")
       .setProperty("messagetrigger").constant("questionnaireresponse")
       .setProperty("component").simple("${routeId}")
       .setProperty("camelID").simple("${camelId}")
       .setProperty("exchangeID").simple("${exchangeId}")
       .setProperty("internalMsgID").simple("${id}")
       .setProperty("bodyData").simple("${body}")
       .setProperty("processname").constant("Input")
       .setProperty("auditdetails").constant("Questionnaire Response resource/bundle received")
       // iDAAS DataHub Processing
       .wireTap("direct:auditing")
       // Send To Topic
       .convertBodyTo(String.class).to(getKafkaTopicUri("fhirsvr_questionnaireresponse"))
       // Invoke External FHIR Server
      .choice().when(simple("{{idaas.processToFHIR}}"))
            .setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
            .to(getFHIRServerUri("QuestionnaireResponse"))
            //Process Response
            .convertBodyTo(String.class)
            // set Auditing Properties
            .setProperty("processingtype").constant("data")
            .setProperty("appname").constant("iDAAS-Connect-FHIR")
            .setProperty("industrystd").constant("FHIR")
            .setProperty("messagetrigger").constant("questionnaireresponse")
            .setProperty("component").simple("${routeId}")
            .setProperty("processname").constant("Response")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("auditdetails").constant("Questionnaire Response FHIR response resource/bundle received")
            // iDAAS DataHub Processing
            .wireTap("direct:auditing")
      .endChoice()
    ;
  }
}