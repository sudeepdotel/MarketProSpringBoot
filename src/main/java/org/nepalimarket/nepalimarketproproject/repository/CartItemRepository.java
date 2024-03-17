package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.CartItem;
import org.nepalimarket.nepalimarketproproject.entity.Item;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndItem ( UserInfo customer, Item item );
}
