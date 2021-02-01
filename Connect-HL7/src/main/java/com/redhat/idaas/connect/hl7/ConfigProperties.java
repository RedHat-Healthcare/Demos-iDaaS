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

    private String kafkaBrokers;

    private int adtPort;

    private int ormPort;

    private int oruPort;

    private int rdePort;

    private int mfnPort;

    private int mdmPort;

    private int schPort;

    private int vxuPort;

    public String getKafkaBrokers() {
        return kafkaBrokers;
    }

    public int getAdtPort() {
        return adtPort;
    }

    public int getOrmPort() {
        return ormPort;
    }

    public int getOruPort() {
        return oruPort;
    }

    public int getRdePort() {
        return rdePort;
    }

    public int getMfnPort() {
        return mfnPort;
    }

    public int getMdmPort() {
        return mdmPort;
    }

    public int getSchPort() {
        return schPort;
    }

    public int getVxuPort() {
        return vxuPort;
    }

    public void setKafkaBrokers(String kafkaBrokers) {
        this.kafkaBrokers = kafkaBrokers;
    }

    public void setAdtPort(int adtPort) {
        this.adtPort = adtPort;
    }

    public void setOrmPort(int ormPort) {
        this.ormPort = ormPort;
    }

    public void setOruPort(int oruPort) {
        this.oruPort = oruPort;
    }

    public void setRdePort(int rdePort) {
        this.rdePort = rdePort;
    }

    public void setMfnPort(int mfnPort) {
        this.mfnPort = mfnPort;
    }

    public void setMdmPort(int mdmPort) {
        this.mdmPort = mdmPort;
    }

    public void setSchPort(int schPort) {
        this.schPort = schPort;
    }

    public void setVxuPort(int vxuPort) {
        this.vxuPort = vxuPort;
    }
}
