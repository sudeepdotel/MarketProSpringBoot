package org.nepalimarket.nepalimarketproproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Category ( String name ) {
        this.name = name;
    }

    public Category ( ) {

    }
}