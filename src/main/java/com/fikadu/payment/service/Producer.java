package com.fikadu.payment.service;

import com.fikadu.payment.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Producer {



    //produce objects for making payment for the order by customer
     Payment payment = new Payment(
             "1111222233334444",
             "05",
             "2024",
             "123",
             "customer1@email.com",
             "32.45",
             "12345",
             "12344532",
             "customer1",
             "12.34",
             "pizzaranch@email.com",
             "4.56",
             "dasher1@email.com",
             "UNPAID",
             "UNPAID"

     );


    @Autowired
    private KafkaTemplate<String, Payment> kafkaTemplate;
    private static final String TOPIC = "doPayment";

    public void publishPayment(){
        kafkaTemplate.send(TOPIC,payment);
    }

    //produce objects for making payments for the restaurant and dasher

    //produce objects for letting the notification that the payment is made


}
