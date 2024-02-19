package org.nepalimarket.nepalimarketproproject.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {


    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank(message = "must provide 5 digit zip code")
    @Size(min = 5, max = 10)
    private String zipCode;
}

