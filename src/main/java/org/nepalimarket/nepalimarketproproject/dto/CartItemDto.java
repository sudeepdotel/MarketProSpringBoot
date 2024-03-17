package org.nepalimarket.nepalimarketproproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long itemId;
    private String itemName;
    private int quantity;
}

