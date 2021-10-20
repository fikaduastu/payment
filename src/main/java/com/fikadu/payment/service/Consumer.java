package com.fikadu.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fikadu.payment.dto.PaymentToDo;
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
}
