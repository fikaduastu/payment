package com.fikadu.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Restaurant {
    private String id;
    private String email;
    private String restaurantName;
    private Address address;
}
