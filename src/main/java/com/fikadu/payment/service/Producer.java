package com.fikadu.payment.service;

import com.fikadu.payment.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Producer {






    @Autowired
    private KafkaTemplate<String, Payment> kafkaTemplate;
    private static final String TOPIC = "doPayment";

    public void publishPayment(){
        //kafkaTemplate.send(TOPIC,payment);
    }

    //produce objects for making payments for the restaurant and dasher

    //produce objects for letting the notification that the payment is made


}
