package org.nepalimarket.nepalimarketproproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class UserInfoRequestDto {

    @NotEmpty
    @Size(max = 60)
    private String fullName;

    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least 8 characters, including at least one uppercase letter, one lowercase letter, and one digit.")
    private String password;

    private AddressDto address;

    private Set<String> role;  // Represent ENUM as a String
}
