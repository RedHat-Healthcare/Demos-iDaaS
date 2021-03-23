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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * FOR TESTING PURPOSES ONLY.
 */
@RestController
public class AuditController {

    private static final Logger log = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private AuditProducer producer;

    @PostMapping("/message")
    public void newMessage(@RequestBody AuditMessage message) {
        Map<String, Object> kafkaHeaders = new HashMap<>();
        kafkaHeaders.put("auditdetails", message.getAuditdetails());
        kafkaHeaders.put("bodyData", message.getBodyData());
        kafkaHeaders.put("camelID", message.getCamelID());
        kafkaHeaders.put("component", message.getComponent());
        kafkaHeaders.put("exchangeID", message.getExchangeID());
        kafkaHeaders.put("industrystd", message.getIndustrystd());
        kafkaHeaders.put("internalMsgID", message.getInternalMsgID());
        kafkaHeaders.put("messageprocesseddate", message.getMessageprocesseddate());
        kafkaHeaders.put("messageprocessedtime", message.getMessageprocessedtime());
        kafkaHeaders.put("messagetrigger", message.getMessagetrigger());
        kafkaHeaders.put("processname", message.getProcessname());
        kafkaHeaders.put("processingtype", message.getProcessingtype());

        producer.sendMessageWithHeaders(message.getBodyData(), kafkaHeaders);
    }
}
