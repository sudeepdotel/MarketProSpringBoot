package org.nepalimarket.nepalimarketproproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    @NotBlank
    private String itemName;

    private String description;


    private Double salesPrice;


    private Double purchasePrice;


    private Integer quantity;


}
