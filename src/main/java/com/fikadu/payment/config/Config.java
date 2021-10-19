package com.fikadu.payment.config;

import com.fikadu.payment.dto.PaymentToDo;
import com.fikadu.payment.dto.PaymentToDoStatus;
import com.fikadu.payment.entity.Payment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class Config {

    // Function to establish a connection
    // between Spring application
    // and Kafka server
    @Bean
    public ConsumerFactory<String, PaymentToDo>
    paymentToDoConsumer()
    {

        // HashMap to store the configurations
        Map<String, Object> map
                = new HashMap<>();

        // put the host IP in the map
        map.put(ConsumerConfig
                        .BOOTSTRAP_SERVERS_CONFIG,
                "127.0.0.1:9092");

        // put the group ID of consumer in the map
        map.put(ConsumerConfig
                        .GROUP_ID_CONFIG,
                "myId");
        map.put(ConsumerConfig
                        .KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        map.put(ConsumerConfig
                        .VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        // return message in JSON formate
        return new DefaultKafkaConsumerFactory<>(
                map, new StringDeserializer(),
                new JsonDeserializer<>(PaymentToDo.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,PaymentToDo> paymentToDoListner()
    {
        ConcurrentKafkaListenerContainerFactory<String,
                PaymentToDo>
                factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentToDoConsumer());
        return factory;
    }


    // this is for producer
    @Bean
    public ProducerFactory<String, PaymentToDoStatus> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, PaymentToDoStatus> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
