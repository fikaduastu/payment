package com.fikadu.payment.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@Component
public class Payment {

    @Id
    @GeneratedValue
    private long id;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvc;
    private String email;
    private String totalCost;
    private String orderId;
    private String userId;
    private String userName;
    private String restaurantCost;
    private String restaurantEmail;
    private String dasherCost;
    private String dasherEmail;
    private String paymentStatusOfUser;
    private String paymentStatusOfRD;

    public Payment(String cardNumber, String expirationMonth, String expirationYear, String cvc, String email, String totalCost, String orderId, String userId, String userName, String restaurantCost, String restaurantEmail, String dasherCost, String dasherEmail, String paymentStatusOfUser, String paymentStatusOfRD) {
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cvc = cvc;
        this.email = email;
        this.totalCost = totalCost;
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.restaurantCost = restaurantCost;
        this.restaurantEmail = restaurantEmail;
        this.dasherCost = dasherCost;
        this.dasherEmail = dasherEmail;
        this.paymentStatusOfUser = paymentStatusOfUser;
        this.paymentStatusOfRD = paymentStatusOfRD;
    }

}
