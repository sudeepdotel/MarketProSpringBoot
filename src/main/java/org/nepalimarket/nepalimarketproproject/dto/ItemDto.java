package org.nepalimarket.nepalimarketproproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    @NotBlank
    private String itemName;

    private String description;

    @NotBlank
    private Double salesPrice;

    @NotBlank
    private Double purchasePrice;

    @NotBlank
    private Integer quantity;





}
