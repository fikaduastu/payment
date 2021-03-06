package com.fikadu.payment.service;

import com.fikadu.payment.dto.Order;
import com.fikadu.payment.dto.PaymentStatus;
import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.dto.PaymentToDoStatus;
import com.fikadu.payment.entity.Payment;
import com.fikadu.payment.repository.PaymentRepository;
import com.fikadu.payment.restaurantDto.DriverInfoToPayment;
import com.fikadu.payment.restaurantDto.OrderToRestaurant;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

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

        String cardNumber = payment.getOrder().getPayment().getCardNumber();
        String expirationMonth = payment.getOrder().getPayment().getExpireMonth();
        String expirationYear = payment.getOrder().getPayment().getExpireYear();
        String cvc = payment.getOrder().getPayment().getCcv();


        Map<String, Object> customerData = new HashMap<String, Object>();
        customerData.put("number", cardNumber);
        customerData.put("exp_month", expirationMonth);
        customerData.put("exp_year", expirationYear);
        customerData.put("cvc", cvc);

        Map<String, Object> params = new HashMap<>();
        params.put("card", customerData);
        Token token = Token.create(params);

        List<String> expandList = new ArrayList<>();
        expandList.add("sources");

        Map<String, Object> retrieveParams = new HashMap<>();
        retrieveParams.put("expand", expandList);

        Customer newCustomer =
                Customer.retrieve(
                        customer.getId(),
                        retrieveParams,
                        null
                );
        Map<String, Object> sourceParams = new HashMap<>();

        sourceParams.put("source", token.getId());
        newCustomer.getSources().create(sourceParams);

        Map<String, Object> params1 = new HashMap<>();
        params1.put("amount", Double.valueOf(payment.getPaymentToMake().get("totalPayment") * 100).longValue());

        params1.put("currency", "usd");
        params1.put("customer", newCustomer.getId());
        params1.put(
                "description",
                "My First Test Charge (created for API docs)"
        );

        Charge charge = Charge.create(params1);
        createPayment(payment);


    }

    public void createPayment(PaymentToDo paymentToDo){
        Random rand = new Random();
        long n = rand.nextLong();
        paymentToDo.getOrder().setId(n);

        payment.setEmail(paymentToDo.getOrder().getUser().getEmail());
        payment.setTotalCost(paymentToDo.getPaymentToMake().get("totalPayment"));
        payment.setRestaurantPrice(paymentToDo.getPaymentToMake().get("restaurantPayment"));
        payment.setDasherPrice(paymentToDo.getPaymentToMake().get("driverPrice"));
        payment.setOrderId(paymentToDo.getOrder().getId());
        payment.setUserId(paymentToDo.getOrder().getUser().getId());
        payment.setUserName(paymentToDo.getOrder().getUser().getUserName());
        payment.setRestaurantEmail(paymentToDo.getOrder().getRestaurant().getEmail());
        payment.setDasherEmail(paymentToDo.getOrder().getDasher().getEmail());
        payment.setPaymentStatusOfUser(PaymentStatus.PAID.name());
        payment.setPaymentStatusOfRD(PaymentStatus.FAILED.name());

        paymentRepository.save(payment);
        Order order = paymentToDo.getOrder();

        PaymentToDoStatus paymentToDoStatus = new PaymentToDoStatus();
        paymentToDoStatus.setPaymentToDo(paymentToDo);
        if (payment.getPaymentStatusOfUser().equals("PAID")) {
            paymentToDoStatus.setPaymentStatus(PaymentStatus.PAID);

           OrderToRestaurant orderToRestaurant = modelMapper.map(order, OrderToRestaurant.class);
            producer.publishRestaurant(orderToRestaurant);
        }
        else
            paymentToDoStatus.setPaymentStatus(PaymentStatus.FAILED);
        producer.publishPayment(paymentToDoStatus);


    }

    public void updateOrder(DriverInfoToPayment driverInfoToPayment) {
        Payment payment =paymentRepository.findByOrderId(driverInfoToPayment.getOrderId());
        payment.setOrderId(driverInfoToPayment.getOrderId());
        payment.setDasherEmail(driverInfoToPayment.getDriver().getEmail());
        payment.setPaymentStatusOfRD(PaymentStatus.PAID.name());
        paymentRepository.save(payment);
    }

    public void cancelOrder(DriverInfoToPayment canceledObeject) {
        Payment payment =paymentRepository.findByOrderId(canceledObeject.getOrderId());
        payment.setPaymentStatusOfRD(PaymentStatus.CANCELED.name());
        payment.setPaymentStatusOfUser(PaymentStatus.CANCELED.name());
        paymentRepository.save(payment);

    }
}
