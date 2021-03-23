/**
 *  Copyright 2005-2018 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.idaas.datahub;

public class AuditMessage {

    public static final String[] DB_PERSISTABLE_FIELDS = new String[] { "messageprocesseddate",
            "messageprocessedtime", "processingtype", "industrystd", "component", "messagetrigger", "processname",
    "auditdetails", "camelID", "exchangeID", "internalMsgID", "bodyData"};

    private String messageprocesseddate;
    private String messageprocessedtime;
    private String processingtype;
    private String industrystd;
    private String component;
    private String messagetrigger;
    private String processname;
    private String auditdetails;
    private String camelID;
    private String exchangeID;
    private String internalMsgID;
    private String bodyData;

    public String getMessageprocesseddate() {
        return messageprocesseddate;
    }

    public void setMessageprocesseddate(String messageprocesseddate) {
        this.messageprocesseddate = messageprocesseddate;
    }

    public String getMessageprocessedtime() {
        return messageprocessedtime;
    }

    public void setMessageprocessedtime(String messageprocessedtime) {
        this.messageprocessedtime = messageprocessedtime;
    }

    public String getProcessingtype() {
        return processingtype;
    }

    public void setProcessingtype(String processingtype) {
        this.processingtype = processingtype;
    }

    public String getIndustrystd() {
        return industrystd;
    }

    public void setIndustrystd(String industrystd) {
        this.industrystd = industrystd;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMessagetrigger() {
        return messagetrigger;
    }

    public void setMessagetrigger(String messagetrigger) {
        this.messagetrigger = messagetrigger;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getAuditdetails() {
        return auditdetails;
    }

    public void setAuditdetails(String auditdetails) {
        this.auditdetails = auditdetails;
    }

    public String getCamelID() {
        return camelID;
    }

    public void setCamelID(String camelID) {
        this.camelID = camelID;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public String getInternalMsgID() {
        return internalMsgID;
    }

    public void setInternalMsgID(String internalMsgID) {
        this.internalMsgID = internalMsgID;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }


}
