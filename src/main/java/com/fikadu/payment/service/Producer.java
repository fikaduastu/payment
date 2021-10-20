package com.fikadu.payment.service;

import com.fikadu.payment.dto.Food;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fikadu.payment.dto.PaymentToDoStatus;


@Service
public class Producer {


    @Autowired
    private KafkaTemplate<String, PaymentToDoStatus> kafkaTemplate;
    private static final String TOPIC = "paymentStatus";

    public void publishPayment(PaymentToDoStatus paymentToDoStatus){

        kafkaTemplate.send(TOPIC,paymentToDoStatus);

    }



}
