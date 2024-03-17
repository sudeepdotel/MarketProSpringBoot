package org.nepalimarket.nepalimarketproproject.service;

import org.hibernate.cache.spi.access.UnknownAccessTypeException;
import org.nepalimarket.nepalimarketproproject.dto.CartItemDto;
import org.nepalimarket.nepalimarketproproject.entity.CartItem;
import org.nepalimarket.nepalimarketproproject.entity.Item;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.exception.OutOfStockException;
import org.nepalimarket.nepalimarketproproject.mapper.CartItemMapper;
import org.nepalimarket.nepalimarketproproject.repository.CartItemRepository;
import org.nepalimarket.nepalimarketproproject.repository.ItemRepository;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserInfoRepository userRepository;
    private final CartItemMapper cartItemMapper; // Assume you have a CartItemMapper

    @Autowired
    public CartService ( CartItemRepository cartItemRepository, ItemRepository itemRepository, UserInfoRepository userRepository, CartItemMapper cartItemMapper ) {
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public CartItemDto addToCart ( Long itemId, String loggedInUsername, int quantity ) {
        // Check if the logged-in user has the Customer role
        boolean isCustomer = userRepository.existsByEmailAndRoleRoleName ( loggedInUsername, UserRole.CUSTOMER );

        if (!isCustomer) {
            // Throw an exception if the user doesn't have the customer role
            throw new UnknownAccessTypeException ( "Only customers can add items to the cart." );
        }

        // Get the user
        UserInfo customer = userRepository.findByEmail ( loggedInUsername )
                .orElseThrow ( ( ) -> new RuntimeException ( "User not found: " + loggedInUsername ) );

        // Get the item
        Item item = itemRepository.findById ( itemId )
                .orElseThrow ( ( ) -> new RuntimeException ( "Item not found: " + itemId ) );

        if (item.getQuantity ( ) < quantity) {
            throw new OutOfStockException ( "Item is out of stock" );
        }
        // Check if the item is already in the cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndItem ( customer, item );

        CartItem cartItem;
        if (existingCartItem.isPresent ( )) {
            // If the item is already in the cart, update the quantity
            cartItem = existingCartItem.get ( );
            cartItem.setQuantity ( cartItem.getQuantity ( ) + quantity );
        } else {
            // If the item is not in the cart, create a new cart item
            cartItem = new CartItem ( );
            cartItem.setItem ( item );
            cartItem.setUser ( customer );
            cartItem.setQuantity ( quantity );
        }
        cartItemRepository.save ( cartItem );

        // Create a CartItemDto to return to the client
        return cartItemMapper.cartItemToCartItemDto ( item, quantity );
    }
}
