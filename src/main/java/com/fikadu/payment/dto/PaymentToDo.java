package com.fikadu.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentToDo {
    private Order order;
    private Map<String, Double> paymentToMake;
}
