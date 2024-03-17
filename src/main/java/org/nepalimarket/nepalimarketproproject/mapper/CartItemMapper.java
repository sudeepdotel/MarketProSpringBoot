package org.nepalimarket.nepalimarketproproject.mapper;

import org.mapstruct.Mapper;
import org.nepalimarket.nepalimarketproproject.dto.CartItemDto;
import org.nepalimarket.nepalimarketproproject.entity.Item;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto cartItemToCartItemDto ( Item item, int quantity );
}
