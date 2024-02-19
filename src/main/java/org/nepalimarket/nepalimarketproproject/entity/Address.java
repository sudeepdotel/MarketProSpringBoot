package org.nepalimarket.nepalimarketproproject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_address")
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "street")
    @NotBlank
    private String street;

    @Column(name = "city")
    @NotBlank
    private String city;

    @NotBlank
    @Column(name = "state")
    private String state;

    @NotBlank(message = "must provide 5 digit zip code")
    @Size(min = 5, max = 10)
    private String zipCode;


}
