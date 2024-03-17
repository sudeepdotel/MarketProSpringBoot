package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreetAndCityAndStateAndZipCode ( String street, String city, String state, String zipCode );
}
