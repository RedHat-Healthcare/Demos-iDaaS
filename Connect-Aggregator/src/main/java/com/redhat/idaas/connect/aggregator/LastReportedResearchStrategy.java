package com.redhat.idaas.connect.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Date;

public class LastReportedResearchStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange == null) {
            return oldExchange;
        } else if (oldExchange == null) {
            return newExchange;
        } else {
            AggregatorResearch oldResearch = oldExchange.getIn(AggregatorResearch.class);
            AggregatorResearch newResearch = newExchange.getIn(AggregatorResearch.class);
            if (newResearch == null) {
                return oldExchange;
            } else if (oldResearch == null) {
                return newExchange;
            } else {
                Date oldResearchDate = (oldResearch.getReportedDateTime() != null) ? oldResearch.getReportedDateTime() : new Date();
                Date newResearchDate = (newResearch.getReportedDateTime() != null) ? newResearch.getReportedDateTime() : new Date();

                return oldResearchDate.after(newResearchDate) ? oldExchange : newExchange;
            }
        }
    }
}
