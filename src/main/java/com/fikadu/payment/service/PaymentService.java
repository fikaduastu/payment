package com.fikadu.payment.service;

import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.entity.Payment;
import com.fikadu.payment.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    Payment payment;

    @Value("${stripe.key}")
    private String stripePublicKey;


    public void createCustomer(PaymentToDo payment) throws StripeException {

        String email = payment.getOrder().getUser().getEmail();
        Customer customer;

        Map<String, Object> options = new HashMap<>();
        options.put("email", email);
        List<Customer> customers = Customer.list(options).getData();

        if (customers.size() > 0) {
            customer = customers.get(0);
            makePayment(customer,payment);
        }
        else {

            Map<String,Object> customerParam = new HashMap<String,Object>();
            customerParam.put("email",email);

            Customer newCustomer = Customer.create(customerParam);

            Map<String,Object> cardParam = new HashMap<String,Object>();
            cardParam.put("number",payment.getOrder().getPayment().getCardNumber());
            cardParam.put("exp_month",payment.getOrder().getPayment().getExpireMonth());
            cardParam.put("exp_year",payment.getOrder().getPayment().getExpireYear());
            cardParam.put("cvc",payment.getOrder().getPayment().getCcv());

            Map<String,Object> tokenParam = new HashMap<String,Object>();
            tokenParam.put("card",cardParam);
            Token token = Token.create(tokenParam);

            Map<String,Object> source = new HashMap<String,Object>();
            source.put("source",token.getId());
            newCustomer.getSources().create(source);

            makePayment(newCustomer,payment);
        }
    }

    public void makePayment(Customer customer,PaymentToDo payment) throws StripeException {

        Map<String,Object> chargeParam = new HashMap<String,Object>();
        chargeParam.put("amount",payment.getPaymentToMake().get("totalPrice"));
        chargeParam.put("currency","usd");
        chargeParam.put("customer",customer.getId());
        Charge.create(chargeParam);
    }

    public void createPayment(PaymentToDo paymentToDo){

        payment.setEmail(paymentToDo.getOrder().getUser().getEmail());
        payment.setTotalCost(paymentToDo.getPaymentToMake().get("totalCost"));
        payment.setRestaurantPrice(paymentToDo.getPaymentToMake().get("dasherPrice"));
        payment.setDasherPrice(paymentToDo.getPaymentToMake().get("restaurantPrice"));
        payment.setOrderId(paymentToDo.getOrder().getId());
        payment.setUserId(paymentToDo.getOrder().getUser().getId());
        payment.setUserName(paymentToDo.getOrder().getUser().getUserName());
        payment.setRestaurantEmail(paymentToDo.getOrder().getRestaurant().getEmail());
        payment.setDasherEmail(paymentToDo.getOrder().getDasher().getEmail());
        payment.setPaymentStatusOfUser("PAID");
        payment.setPaymentStatusOfRD("UNPAID");

        paymentRepository.save(payment);

    }
}
