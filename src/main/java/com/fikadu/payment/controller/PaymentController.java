package com.fikadu.payment.controller;


import com.fikadu.payment.dto.*;
import com.fikadu.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {



    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/")
    public void method() throws StripeException {
        Food food1 = new Food("crispy chicken burger",10.25);
        Food food2 = new Food("ham burger",7.75);
        List<Food> list = new ArrayList<>();
        list.add(food1);
        list.add(food2);

        User user = new User(1234,"customer1","customer1@email.com",null);
        Restaurant restaurant = new Restaurant("123","restaurant1@email.com","pizzaranch",null);
        Payment payment = new Payment("4242424242424242","12","2022","112");
        Dasher dasher = new Dasher();
        Order order = new Order(123L,user,list,restaurant,payment,dasher);
        Map<String,Double> map = new HashMap<>();
        /*map.put("dasherPrice",Double.valueOf(2.45).longValue());
        map.put("restaurantPrice",Double.valueOf(18).longValue());
        map.put("totalPrice",Double.valueOf(25).longValue());
*/
        PaymentToDo paymentToDo = new PaymentToDo(order,map);
        paymentService.createCustomer(paymentToDo);

    }



}
