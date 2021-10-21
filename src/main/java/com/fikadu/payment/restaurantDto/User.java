package com.fikadu.payment.restaurantDto;

import com.fikadu.payment.dto.Address;
import lombok.Data;

@Data
public class User {
//    private long userId;
//    private String userName;
//    private String email;
    private long id;
    private String userName;
    private String email;
    private Address address;
}
