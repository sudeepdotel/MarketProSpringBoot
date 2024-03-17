package org.nepalimarket.nepalimarketproproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveItemRequestDto {

    @NotBlank
    private String categoryName;

    @NotBlank
    private String vendorName;


    @Valid
    private List<ItemDto> items;


}
