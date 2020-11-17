package com.redhat.idaas.dream.demo;

import io.connectedhealth_idaas.eventbuilder.events.platform.RoutingEvent;

public class TestUtils {
    public static RoutingEvent routingEvent(String sendingApp, String messageType, String messageEvent) {
        RoutingEvent routingEvent = new RoutingEvent();
        routingEvent.setSendingApp(sendingApp);
        routingEvent.setMessageType(messageType);
        routingEvent.setMessageEvent(messageEvent);
        return routingEvent;
    }
}
