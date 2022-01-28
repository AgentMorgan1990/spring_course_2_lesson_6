package com.geekbrains.spring.web.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.geekbrains.spring.web.api.dto.ProductDto;

@Service
public class CartService {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public ProductDto findProductById(Long productId) {

        ProductDto productDto =
                restTemplate.getForObject("http://localhost:8189//web-market-cart/api/v1/carts/" + productId,
                        ProductDto.class);
        return productDto;
    }
}
