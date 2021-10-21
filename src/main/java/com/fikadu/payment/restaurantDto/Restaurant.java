package com.fikadu.payment.restaurantDto;

import com.fikadu.payment.dto.Address;
import lombok.Data;

@Data
public class Restaurant {
//    private String restaurantId;
//    private String restaurantName;

    private String id;
    private String email;
    private String restaurantName;
    private Address address;
}
