package com.mikehenry.rabbitmqdataconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikehenry.rabbitmqdataconsumer.configuration.ApplicationConfiguration;
import com.mikehenry.rabbitmqdataconsumer.dto.DataRequest;
import com.mikehenry.rabbitmqdataconsumer.processor.RequestProcessor;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataConsumer implements ChannelAwareMessageListener {

    ApplicationConfiguration appConfigs = new ApplicationConfiguration();

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Logger logger = LoggerFactory.getLogger(DataConsumer.class);

        logger.info("================DELIVERY TAG:"+message.getMessageProperties().getDeliveryTag()+"=============");

        logger.info("Request from Queue: " + new String(message.getBody()));

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map requestDataMap =  objectMapper.readValue(message.getBody(), Map.class);

            DataRequest dataRequest = DataRequest.builder()
                    .employeeName(String.valueOf(requestDataMap.get("employeeName")))
                    .salary(Double.parseDouble(String.valueOf(requestDataMap.get("salary"))))
                    .age(Integer.parseInt(String.valueOf(requestDataMap.get("age"))))
                    .build();

            boolean status = processRequest(dataRequest);

        } catch (ListenerExecutionFailedException e) {
            logger.error("Exception getting message from queue: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("================ACKNOWLEDGING D.TAG:"+message.getMessageProperties().getDeliveryTag()+"=============");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * To process the request i.e. Send create employee request to endpoint and process response received
     * @param dataRequest Object of type DataRequest
     * @return boolean true | false depending on outcome of processing request
     */
    private boolean processRequest(DataRequest dataRequest) {
        Logger logger = LoggerFactory.getLogger(DataConsumer.class);
        try {
            RequestProcessor requestProcessor = new RequestProcessor();

            Map<Object, Object> paramMap = new HashMap<>();
            paramMap.put("name", dataRequest.getEmployeeName());
            paramMap.put("salary", dataRequest.getSalary());
            paramMap.put("age", dataRequest.getAge());

            ObjectMapper dataMapper = new ObjectMapper();
            String params = dataMapper.writeValueAsString(paramMap);

            logger.info("Request Parameters: " + params);

            String response = requestProcessor.processRequest(appConfigs.getEmployeeUrlCreate(), params);

            Map mapResponse = processResponse(response);

            boolean successStatus = false;
            if (mapResponse.containsKey("status")) {
                if (mapResponse.get("status").equals("success")) {
                    logger.info("Employee was successfully created");
                } else {
                    logger.error("Could not create employee");
                }
            }

            return successStatus;
        } catch (JsonProcessingException e) {
            logger.error("Error converting request to JSON: " + e.getMessage());
        }

        return false;
    }

    /**
     * To build the response to a map
     * @param response String Response received from url
     * @return Map
     */
    private Map processResponse(String response) {
        Logger logger = LoggerFactory.getLogger(DataConsumer.class);
        Map responseMap = new HashMap();
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseMap = mapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            logger.error("Exception extracting data: " + e.getMessage());
        }

        return responseMap;
    }
}
