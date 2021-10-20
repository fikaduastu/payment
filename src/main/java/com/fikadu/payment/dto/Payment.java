package com.fikadu.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payment {
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String ccv;
}
