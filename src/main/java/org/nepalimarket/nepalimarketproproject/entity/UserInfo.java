package org.nepalimarket.nepalimarketproproject.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.nepalimarket.nepalimarketproproject.configuration.SpringSecurityConfig;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty
    @Size(max = 60)
    private String fullName;

    @Pattern ( regexp = "^\\d{10}$")
    private String phone;

    @Email
    @Column(unique = true)
    private String email;

    @Pattern ( regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$" , message = "\"Password must contain at least 8 characters, including at least one uppercase letter, one lowercase letter, and one digit.\"")
    private String password;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Address address;

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> role;


    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;


}
