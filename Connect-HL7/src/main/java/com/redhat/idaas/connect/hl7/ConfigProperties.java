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
package com.redhat.idaas.connect.hl7;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "idaas")
public class ConfigProperties {

    //Variables
    // Kafka
    private String kafkaBrokers;
    //HL7 Ports
    private int adtPort;
    private int ormPort;
    private int oruPort;

    // HL7 Directories
    private String hl7ADT_Directory;

    // Platform Topics
    private String adtTopicName;
    private String ormTopicName;
    private String oruTopicName;

    // Getters
    // Getters: Kafka Brokers
    public String getKafkaBrokers() { return kafkaBrokers; }
    // Getters: HL7 Ports
    public int getAdtPort() { return adtPort; }
    public int getOrmPort() { return ormPort; }
    public int getOruPort() { return oruPort; }

    // Getters: HL7 Directories
    public String getHl7ADT_Directory() { return hl7ADT_Directory; }

    // Getters: Platform Topics
    public String getadtTopicName() { return adtTopicName; }
    public String getormTopicName() { return ormTopicName; }
    public String getoruTopicName() { return oruTopicName; }

    // Setters
    // Setters: Kafka Brokers
    public void setKafkaBrokers(String kafkaBrokers) { this.kafkaBrokers = kafkaBrokers; }
    // Setters: HL7 Ports
    public void setAdtPort(int adtPort) { this.adtPort = adtPort; }
    public void setOrmPort(int ormPort) { this.ormPort = ormPort; }
    public void setOruPort(int oruPort) { this.oruPort = oruPort; }

    // Setters: HL7 Directories
    public void setHl7ADT_Directory(String hl7ADT_Directory) { this.hl7ADT_Directory = hl7ADT_Directory; }

    // Setters: Kafka Topics
    public void setadtTopicName(String adtTopicName) { this.adtTopicName = adtTopicName; }
    public void setormTopicName(String ormTopicName) { this.ormTopicName = ormTopicName; }
    public void setoruTopicName(String oruTopicName) { this.oruTopicName = oruTopicName; }


}
