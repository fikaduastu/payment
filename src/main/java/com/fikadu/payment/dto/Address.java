package com.fikadu.payment.dto;

import lombok.Data;

@Data
public class Address {
    private String zipCode;
    private String city;
    private String hoseNumber;
    private String streetName;
    private double latitude;
    private double longitude;
}
