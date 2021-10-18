package com.fikadu.payment.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Order {
    private User user;
    private List<Food> foods;
    private Restaurant restaurant;
    private Payment payment;
}
