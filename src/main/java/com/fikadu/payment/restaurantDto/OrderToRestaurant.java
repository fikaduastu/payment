package com.fikadu.payment.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderToRestaurant {
    private long orderId;
    private User user;
    private List<Food> foods;
    private Restaurant restaurant;
}