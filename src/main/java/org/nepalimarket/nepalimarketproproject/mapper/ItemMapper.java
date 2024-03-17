package org.nepalimarket.nepalimarketproproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nepalimarket.nepalimarketproproject.dto.ItemDto;
import org.nepalimarket.nepalimarketproproject.dto.ItemResponseDto;
import org.nepalimarket.nepalimarketproproject.dto.SaveItemRequestDto;
import org.nepalimarket.nepalimarketproproject.entity.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ItemMapper {


    //@Mapping(target = "dateOfEntry", expression = "java(new Date())")
    Item itemDtoToItem ( ItemDto itemDto );

    ItemDto itemToItemDto ( Item item );

    @Mapping(target = "price", source = "salesPrice")
    @Mapping(target = "categoryName", source = "category.name")
    ItemResponseDto itemToItemResponseDto ( Item item );


    @Mapping(target = "category.name", source = "categoryName")
    @Mapping(target = "category", ignore = true)
    Item saveItemRequestDtoToItem ( SaveItemRequestDto saveItemRequestDto );

    default List<Item> saveItemRequestDtoToItems ( SaveItemRequestDto saveItemRequestDto ) {
        if (saveItemRequestDto == null || saveItemRequestDto.getItems ( ) == null) {
            return Collections.emptyList ( );
        }

        return saveItemRequestDto.getItems ( ).stream ( )
                .map ( this::itemDtoToItem )
                .collect ( Collectors.toList ( ) );
    }

}
