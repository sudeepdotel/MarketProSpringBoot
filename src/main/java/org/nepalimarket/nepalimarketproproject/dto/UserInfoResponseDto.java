package org.nepalimarket.nepalimarketproproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserInfoResponseDto {

    private Long userId;

    private String fullName;

    private String phone;

    private String email;

    private AddressDto address;

    private Set<String> role;  // Represent the Enum as a string


}
