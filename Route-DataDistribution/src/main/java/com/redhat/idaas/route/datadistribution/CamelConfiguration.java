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
package com.redhat.idaas.route.datadistribution;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.connection.JmsTransactionManager;
//import javax.jms.ConnectionFactory;
import org.springframework.stereotype.Component;

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

  private String getKafkaTopicUri(String topic) {
    return "kafka:" + topic +
            "?brokers=" +
            config.getKafkaBrokers();
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
        //.convertBodyTo(String.class).to("kafka://localhost:9092?topic=opsMgmt_PlatformTransactions&brokers=localhost:9092")
        .convertBodyTo(String.class).to(getKafkaTopicUri("opsmgmt_platformtransactions"))
    ;
    /*
    *  Logging
    */
    from("direct:logging")
      .log(LoggingLevel.INFO, log, "Transaction Message: [${body}]")
    ;

    /*
    *   Simple language reference
    *   https://camel.apache.org/components/latest/languages/simple-language.html
    */
    /*
     *   Middle Tier
     *   Move Transactions and enable the Clinical Data Enterprise Integration Pattern
     *   HL7 v2
     *   1. to Sending App By Message Type
     *   2. to Facility By Message Type
     *   3. to Enterprise by Message Type
     *   FHIR
     *   1. to Enterprise by Message Type
     */

    /*
     *    HL7v2 ADT
     */
    from(getKafkaTopicUri("mctn_mms_adt"))
       .routeId("ADT-MiddleTier")
       // Auditing
       .setProperty("processingtype").constant("data")
       .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
       .setProperty("industrystd").constant("HL7")
       .setProperty("messagetrigger").constant("ADT")
       .setProperty("component").simple("${routeId}")
       .setProperty("processname").constant("MTier")
       .setProperty("auditdetails").constant("ADT to Enterprise By Sending App By Data Type middle tier")
       .wireTap("direct:auditing")
       // Enterprise Message By Sending App By Type
       .convertBodyTo(String.class).to(getKafkaTopicUri("mms_adt"))
       //.to("kafka:localhost:9092?topic=mms_adt&brokers=localhost:9092")
       // Auditing
       .setProperty("processingtype").constant("data")
       .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
       .setProperty("industrystd").constant("HL7")
       .setProperty("messagetrigger").constant("ADT")
       .setProperty("component").simple("${routeId}")
       .setProperty("camelID").simple("${camelId}")
       .setProperty("exchangeID").simple("${exchangeId}")
       .setProperty("internalMsgID").simple("${id}")
       .setProperty("bodyData").simple("${body}")
       .setProperty("processname").constant("MTier")
       .setProperty("auditdetails").constant("ADT to Facility By Sending App By Data Type middle tier")
       .wireTap("direct:auditing")
       // Facility By Type
       .convertBodyTo(String.class).to(getKafkaTopicUri("mctn_adt"))
       // Auditing
       .setProperty("processingtype").constant("data")
       .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
       .setProperty("industrystd").constant("HL7")
       .setProperty("messagetrigger").constant("ADT")
       .setProperty("component").simple("${routeId}")
       .setProperty("camelID").simple("${camelId}")
       .setProperty("exchangeID").simple("${exchangeId}")
       .setProperty("internalMsgID").simple("${id}")
       .setProperty("bodyData").simple("${body}")
       .setProperty("processname").constant("MTier")
       .setProperty("auditdetails").constant("ADT to Enterprise By Sending App By Data Type middle tier")
       .wireTap("direct:auditing")
       // Enterprise Message By Type
       .convertBodyTo(String.class).to(getKafkaTopicUri("ent_adt"))
    ;

    /*
     *   HL7v2 ORM
     */
    from(getKafkaTopicUri("mctn_mms_orm"))
        .routeId("ORM-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORM")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ORM to Enterprise Sending App By Data Type middle tier")
        .wireTap("direct:auditing")
         // Enterprise Message By Sending App By Type
         .convertBodyTo(String.class).to(getKafkaTopicUri("mms_orm"))
         // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORM")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ORM to Facility By Data Type middle tier")
        .wireTap("direct:auditing")
        // Facility By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("mctn_orm"))
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORM")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ADT to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_orm"))
    ;

    /*
     *   HL7v2 ORU
     */
    from(getKafkaTopicUri("mctn_mms_oru"))
        .routeId("ORU-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORU")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ORU to Enterprise Sending App By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Sending App By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("mms_oru"))
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORU")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ORU Facility By Data Type middle tier")
        .wireTap("direct:auditing")
        // Facility By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("mctn_oru"))
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("HL7")
        .setProperty("messagetrigger").constant("ORU")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("ORU to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Entrprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_oru"))
    ;


    /*
     *   FHIR
     */
    // Adverse Events
    from(getKafkaTopicUri("fhirsvr_adverseevent"))
        //from("kafka:fhirsvr_AdverseEvent?brokers=localhost:9092")
        .routeId("AdverseEvent-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("AdverseEvent")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("Adverse Event to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_fhirsvr_adverseevent"))
    ;

    // Allergy Intollerance
    from(getKafkaTopicUri("fhirsvr_allergyintollerance"))
        .routeId("AllergyIntollerance-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("AllergyIntollerance")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("Allergy Intollerance to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_fhirsvr_allergyintollerance"))
    ;

    // Appointment
    from(getKafkaTopicUri("fhirsvr_appointment"))
        .routeId("Appointment-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("Appointment")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("Appointment to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_fhirsvr_appointment"))
    ;

    // Appointment Response
    from(getKafkaTopicUri("fhirsvr_appointmentresponse"))
        .routeId("AppointmentReesponse-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("AppointmentResponse")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("Appointment Response to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to(getKafkaTopicUri("ent_fhirsvr_appointmentresponse"))
    ;

    //Care Plan
    from("kafka:localhost:9092?topic=fhirsvr_careplan&brokers=localhost:9092")
        .routeId("CarePlan-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("CarePlan")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("Care Plan to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to("kafka:localhost:9092?topic=ent_fhirsvr_careplan&brokers=localhost:9092")
    ;
    //Care Team
    from("kafka:localhost:9092?topic=fhirsvr_CareTeam&brokers=localhost:9092")
        .routeId("CareTeam-MiddleTier")
        // Auditing
        .setProperty("processingtype").constant("data")
        .setProperty("appname").constant("iDAAS-ConnectClinical-IndustryStd")
        .setProperty("industrystd").constant("FHIR")
        .setProperty("messagetrigger").constant("CareTeam")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("MTier")
        .setProperty("auditdetails").constant("CareTeam to Enterprise By Data Type middle tier")
        .wireTap("direct:auditing")
        // Enterprise Message By Type
        .convertBodyTo(String.class).to("kafka:localhost:9092?topic=ent_fhirsvr_CareTeam&brokers=localhost:9092")
    ;

  }
}