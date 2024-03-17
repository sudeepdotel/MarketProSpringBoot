package org.nepalimarket.nepalimarketproproject.controller;

import org.nepalimarket.nepalimarketproproject.dto.CartItemDto;
import org.nepalimarket.nepalimarketproproject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {


    private final CartService cartService;

    @Autowired
    public CartController ( CartService cartService ) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDto> addToCart ( @RequestParam Long itemId,
                                                   @RequestParam int quantity,
                                                   @AuthenticationPrincipal UserDetails userDetails ) {
        String loggedInUsername = userDetails.getUsername ( );
        CartItemDto cartItemDto = cartService.addToCart ( itemId, loggedInUsername, quantity );
        if (cartItemDto != null) {
            return ResponseEntity.ok ( cartItemDto );
        } else {
            return ResponseEntity.badRequest ( ).build ( );
        }

    }
}
