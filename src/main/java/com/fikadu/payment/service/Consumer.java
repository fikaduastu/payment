package com.fikadu.payment.service;

import com.fikadu.payment.dto.PaymentToDo;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {


    @Autowired
    PaymentService paymentService;


    //@KafkaListener(id = "myId", topics = "order")
    public void consumeFromKafka(PaymentToDo payment) throws StripeException {
        paymentService.createCustomer(payment);
    }
}
