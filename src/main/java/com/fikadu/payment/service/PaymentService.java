package com.fikadu.payment.service;

import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.dto.PaymentToDoStatus;
import com.fikadu.payment.entity.Payment;
import com.fikadu.payment.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    Payment payment;

    @Autowired
    Producer producer;





    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }




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

            Map<String,Object> customerParam = new HashMap<>();
            customerParam.put("email",email);
            customerParam.put("payment_method", "pm_card_visa");

            Customer newCustomer = Customer.create(customerParam);

            Map<String,Object> cardParam = new HashMap<>();
            cardParam.put("number",payment.getOrder().getPayment().getCardNumber());
            cardParam.put("exp_month",payment.getOrder().getPayment().getExpireMonth());
            cardParam.put("exp_year",payment.getOrder().getPayment().getExpireYear());
            cardParam.put("cvc",payment.getOrder().getPayment().getCcv());

            Map<String,Object> tokenParam = new HashMap<>();
            tokenParam.put("card",cardParam);
            Token token = Token.create(tokenParam);

            Map<String,Object> source = new HashMap<>();
            source.put("source",token.getId());
            try{
            newCustomer.getSources().create(source);
            }
            catch (Exception e){

            }

            makePayment(newCustomer,payment);
        }
    }

    public void makePayment(Customer customer,PaymentToDo payment) throws StripeException {

//        Map<String,Object> chargeParam = new HashMap<>();
//        chargeParam.put("amount",payment.getPaymentToMake().get("totalPrice"));
//        chargeParam.put("currency","usd");
//        chargeParam.put("customer",customer.getId());
//        Charge.create(chargeParam);
//        createPayment(payment);

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(100L)
                        .setCurrency("usd")
                        .addPaymentMethodType("card")
                        .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

//        ChargeCreateParams chargeParams =
//                ChargeCreateParams.builder()
//                        .setAmount(payment.getPaymentToMake().get("totalPrice"))
//                        .setCurrency("usd")
//                        .setCustomer(customer.getId())
//                        .build();
//        Charge charge = Charge.create(chargeParams);
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

        PaymentToDoStatus paymentToDoStatus = new PaymentToDoStatus();
        paymentToDoStatus.setPaymentToDo(paymentToDo);
        paymentToDoStatus.setStatus(payment.getPaymentStatusOfUser());
        producer.publishPayment(paymentToDoStatus);

    }
}
