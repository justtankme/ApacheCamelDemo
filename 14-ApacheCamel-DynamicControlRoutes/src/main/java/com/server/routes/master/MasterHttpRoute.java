package com.server.routes.master;

import com.server.DynamicControlRoutesMainApp;
import com.server.dto.PersonDTO;
import com.server.processor.HttpResponseContentTypeProcessor;
import com.server.util.CommonUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteConfigurationDefinition;
import org.apache.camel.model.dataformat.ContentTypeHeaderAware;
import org.apache.camel.processor.aggregate.StringAggregationStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * master路由-用来控制Slave路由的启动和停止
 *
 * @author CYX
 * @date 2018/11/7 15:26
 */
public class MasterHttpRoute extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterHttpRoute.class);

    private CamelContext camelContext = DynamicControlRoutesMainApp.camelContext;

    @Override
    public void configure() throws Exception {

        /**
         * master路由
         * 该路由控制slave中的其他路由
         * http://127.0.0.1:8282/masterRoute?slaveFlag=2
         */
        from("jetty:http://127.0.0.1:8282/masterRoute").routeId("masterRoute").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {

//                InputStream bodyStream = exchange.getIn().getBody(InputStream.class);
//                String bodyStr = IOUtils.toString(bodyStream, "UTF-8");
//                LOGGER.info("bodyStr : {}", new Object[]{bodyStr});
                Map map = exchange.getIn().getHeaders();

                LOGGER.info("header map : {}", map);

                PersonDTO personDTO = CommonUtils.toJavaObject(map, PersonDTO.class);

                if ("0".equals(personDTO.getSlaveFlag())) {

                    //停止'test-route-control-03'路由

                    camelContext.getRouteController().stopRoute("test-route-control-03");
                    LOGGER.info("===== 停止 routeID:test-route-control-03 =====");

                } else if ("1".equals(personDTO.getSlaveFlag())) {

                    //启动'test-route-control-03'路由

                    camelContext.getRouteController().startRoute("test-route-control-03");
                    LOGGER.info("===== 启动 routeID:test-route-control-03 =====");

                } else if ("2".equals(personDTO.getSlaveFlag())) {

                    //暂停'test-route-control-03'路由

                    camelContext.getRouteController().suspendRoute("test-route-control-03", 20, TimeUnit.SECONDS);
                    LOGGER.info("===== 暂停 routeID:test-route-control-03 =====");
                } else if ("3".equals(personDTO.getSlaveFlag())) {

                    //恢复'test-route-control-03'路由

                    camelContext.getRouteController().resumeRoute("test-route-control-03");
                    LOGGER.info("===== 恢复 routeID:test-route-control-03 =====");
                }

                if (exchange.getPattern() == ExchangePattern.InOut) {
                    Message outMessage = exchange.getMessage();
                    outMessage.setBody("received msg中文 " + "-00");
                }
            }
        }).process(new HttpResponseContentTypeProcessor())
;
    }

}