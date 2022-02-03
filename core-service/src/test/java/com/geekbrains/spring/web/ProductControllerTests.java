package com.geekbrains.spring.web;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekbrains.spring.web.core.SpringWebApplication;
import com.geekbrains.spring.web.core.dto.ProductDto;
import com.geekbrains.spring.web.core.entities.Product;
import com.geekbrains.spring.web.core.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@SpringBootTest(classes = SpringWebApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ProductsService productsService;

    @Test
    void createAccountByNewClient() throws Exception {
        Product product = new Product(
                1L,
                "Milk",
                500
        );

        Mockito.doReturn(Optional.of(product)).when(productsService).findById(1L);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE);


        MvcResult requestResult = mockMvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ProductDto objectResponse = objectMapper.readValue(
                requestResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        log.info("Название продукта в ответе: " + objectResponse.getTitle());
        log.info("Цена продукта в ответе: " + objectResponse.getPrice());
        Assertions.assertEquals("Milk", objectResponse.getTitle());
        Assertions.assertEquals(500, objectResponse.getPrice());
    }
}
