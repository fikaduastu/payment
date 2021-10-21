package com.fikadu.payment.dto;

import lombok.Data;

@Data
public class PaymentToDoStatus {

    private PaymentToDo paymentToDo;
    private PaymentStatus paymentStatus;
}
