package com.fikadu.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.restaurantDto.DriverInfoToPayment;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {


    @Autowired
    PaymentService paymentService;

    ObjectMapper objectMapper=new ObjectMapper();

    @KafkaListener(topics = "order",id = "myId")
    public void consumeFromKafka(String payment) {
        System.out.println(payment);
        PaymentToDo paymentToDo = null;
        try {
            paymentToDo = objectMapper.readValue(payment, PaymentToDo.class);
            paymentService.createCustomer(paymentToDo);
        } catch (JsonProcessingException | StripeException e) {
            e.printStackTrace();
        }
    }
    @KafkaListener(topics = "payment",groupId = "myId")
    public void consumeFromDasher(String payment) {
        //System.out.println(payment);
        DriverInfoToPayment driverInfoToPayment = null;

        try {
            driverInfoToPayment = objectMapper.readValue(payment, DriverInfoToPayment.class);
            paymentService.updateOrder(driverInfoToPayment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
    @KafkaListener(topics = "paymentCancel",groupId = "myId")
    public void consumeCancelOrder(String cancelOrder){
        DriverInfoToPayment canceledObeject = null;
        try{
            canceledObeject = objectMapper.readValue(cancelOrder,DriverInfoToPayment.class);
            paymentService.cancelOrder(canceledObeject);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

}
