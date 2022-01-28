package com.geekbrains.spring.web.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private String productTitle;
    private int quantity;
    private int pricePerProduct;
    private int price;

    public OrderItemDto(ProductDto productdto) {
        this.productId = productdto.getId();
        this.productTitle = productdto.getTitle();
        this.quantity = 1;
        this.pricePerProduct = productdto.getPrice();
        this.price = productdto.getPrice();
    }

    public void changeQuantity(int delta) {
        this.quantity += delta;
        this.price = this.quantity * this.pricePerProduct;
    }
}
