package com.fikadu.payment.entity;



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
    private Long id;
    private Long userId;
    private Long orderId;
    private Long totalCost;
    private Long dasherPrice;
    private Long restaurantPrice;
    private String email;
    private String userName;
    private String restaurantEmail;
    private String dasherEmail;
    private String paymentStatusOfUser;
    private String paymentStatusOfRD;
}
