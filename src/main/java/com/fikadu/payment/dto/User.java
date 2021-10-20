package com.fikadu.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String userName;
    private String email;
    private Address address;
}
