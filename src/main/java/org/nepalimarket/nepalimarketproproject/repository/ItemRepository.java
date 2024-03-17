package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.Category;
import org.nepalimarket.nepalimarketproproject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemName ( String itemName );

    Item findByCategoryAndItemName ( Category category, String itemName );

    Optional<Item> findByCategory ( Category category );

}
