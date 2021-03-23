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

import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.Map;

/**
 * FOR TESTING PURPOSES ONLY.
 */
@Service
public class AuditProducer {

    private static final Logger log = LoggerFactory.getLogger(AuditProducer.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConfigProperties config;

    public void sendMessageWithHeaders(String message, Map<String, Object> headers) {
        producerTemplate.sendBodyAndHeaders(config.getKafkaTopicUri(), message, headers);
    }
}