package com.fikadu.payment.service;

import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.entity.Payment;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    //consume objects so that you are gonna get payed for the order by the customer

    @Autowired
    PaymentService paymentService;

    // this topic and groupId should be from the producer server
    @KafkaListener(topics = "order", groupId = "myPaymentGroup")
    public void consumeFromKafka(PaymentToDo payment) throws StripeException {
        paymentService.createCustomer(payment);
    }

    //consume objects so that you are gonna pay for the restaurant as well as dasher
}
