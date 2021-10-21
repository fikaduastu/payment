package com.fikadu.payment.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * a DTO called when a driver changes the status to delivered
 */
public class DriverInfoToPayment {
    private long orderId;
//    private String restaurantName;
//    private String restaurantEmail;
    private Driver driver;
}
