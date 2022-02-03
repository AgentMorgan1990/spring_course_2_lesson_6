package com.geekbrains.spring.web;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.geekbrains.spring.web.core.SpringWebApplication;
import com.geekbrains.spring.web.core.dto.Cart;
import com.geekbrains.spring.web.core.entities.Product;
import com.geekbrains.spring.web.core.services.CartService;
import com.geekbrains.spring.web.core.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@SpringBootTest(classes = SpringWebApplication.class)
@Slf4j
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @MockBean
    private ProductsService productsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @BeforeEach
    public void initRedis() {
        Cart cart = new Cart();
        Product product = new Product(
                1L,
                "Milk",
                500
        );
        cart.add(product);
        String cartKey = "1";

        redisTemplate.opsForValue().set(cartKey, cart);
    }

    @Test
    void testGetCurrentCart() {
        log.info("Стоимость всей корзины: " + cartService.getCurrentCart("1").getTotalPrice());
        Assertions.assertEquals(500, cartService.getCurrentCart("1").getTotalPrice());
    }

    @Test
    void testAddToCart() {
        Product product = new Product(
                2L,
                "Chocolate",
                200
        );
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(2L);
        cartService.addToCart("1", 2L);

        log.info("Стоимость всей корзины: " + cartService.getCurrentCart("1").getTotalPrice());
        Assertions.assertEquals(700, cartService.getCurrentCart("1").getTotalPrice());
    }

    @Test
    void testCreateNewCart() {
        log.info("Стоимость всей корзины: " + cartService.getCurrentCart("2").getTotalPrice());
        Assertions.assertEquals(0, cartService.getCurrentCart("2").getTotalPrice());
    }

    @Test
    void testCreateKeyWithSuffix() {
        log.info("Сгенерирован ключ: " + cartService.getCartUuidFromSuffix("user"));
        Assertions.assertEquals("SPRING_WEB_user", cartService.getCartUuidFromSuffix("user"));
    }
    @Test
    void testMerge() {
        Product product = new Product(
                2L,
                "Chocolate",
                200
        );
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(2L);
        cartService.addToCart("2",2l);
        log.info("Стоимость всей корзины user-а: " + cartService.getCurrentCart("1").getTotalPrice());
        log.info("Стоимость всей корзины гостя: " + cartService.getCurrentCart("2").getTotalPrice());
        cartService.merge("1","2");
        log.info("Стоимость всей корзины user-а после мержа: " + cartService.getCurrentCart("1").getTotalPrice());
        Assertions.assertEquals(700,cartService.getCurrentCart("1").getTotalPrice());
    }
    @Test
    void testAddSameProduct() {
        Product product = new Product(
                1L,
                "Milk",
                500
        );
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(1L);
        cartService.addToCart("1",1l);
        log.info("Стоимость всей корзины: " + cartService.getCurrentCart("1").getTotalPrice());
        log.info("Кол-во продуктов в корзине: " + cartService.getCurrentCart("1").getItems().size());
        Assertions.assertEquals(1,cartService.getCurrentCart("1").getItems().size());
        Assertions.assertEquals(1000,cartService.getCurrentCart("1").getTotalPrice());
    }
    @Test
    void testClearCart() {
        log.info("Кол-во продуктов в корзине: " + cartService.getCurrentCart("1").getItems().size());
        cartService.clearCart("1");
        log.info("Кол-во продуктов в корзине: " + cartService.getCurrentCart("1").getItems().size());
        Assertions.assertEquals(0,cartService.getCurrentCart("1").getItems().size());
    }
}
