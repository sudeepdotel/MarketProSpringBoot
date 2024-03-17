package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName ( String name );
}
