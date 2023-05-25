package com.server.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class HttpResponseContentTypeProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("content-type", "application/json;utf-8");
    }
}
