package org.nepalimarket.nepalimarketproproject.service;

import org.hibernate.cache.spi.access.UnknownAccessTypeException;
import org.nepalimarket.nepalimarketproproject.dto.ItemDto;
import org.nepalimarket.nepalimarketproproject.dto.SaveItemRequestDto;
import org.nepalimarket.nepalimarketproproject.entity.Category;
import org.nepalimarket.nepalimarketproproject.entity.Item;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.mapper.ItemMapper;
import org.nepalimarket.nepalimarketproproject.repository.CategoryRepository;
import org.nepalimarket.nepalimarketproproject.repository.ItemRepository;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserInfoRepository userRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService ( ItemRepository itemRepository, CategoryRepository categoryRepository,
                         UserService userService, UserInfoRepository userRepository, ItemMapper itemMapper ) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
    }


    public List<ItemDto> addItem ( SaveItemRequestDto requestDto, String loggedInUsername ) {
        // Check if the logged-in user has the Admin role
        boolean isAdmin = userRepository.existsByEmailAndRoleRoleName ( loggedInUsername, UserRole.ADMIN );

        if (!isAdmin) {
            // Throw an exception if the user doesn't have admin role
            throw new UnknownAccessTypeException ( "You don't have permission to add items." );
        }

        // Find or create a category based on the request
        Category category = categoryRepository.findByName ( requestDto.getCategoryName ( ) )
                .orElseGet ( ( ) -> categoryRepository.save ( new Category ( requestDto.getCategoryName ( ) ) ) );

        // List to store the saved ItemDtos
        List<ItemDto> savedItems = new ArrayList<> ( );

        // Loop through the items in the request and save them
        for (ItemDto itemDto : requestDto.getItems ( )) {
            // Create a new Item entity and set its properties from the ItemDto
            Item item = new Item ( );
            item.setItemName ( itemDto.getItemName ( ) );
            item.setDescription ( itemDto.getDescription ( ) );
            item.setSalesPrice ( itemDto.getSalesPrice ( ) );
            item.setPurchasePrice ( itemDto.getPurchasePrice ( ) );
            item.setQuantity ( itemDto.getQuantity ( ) );
            item.setDateOfEntry ( new Date ( ) );
            item.setCategory ( category );

            // Save the item to the database
            Item savedItem = itemRepository.save ( item );

            // Create a new ItemDto and set its properties from the saved Item entity
            ItemDto savedItemDto = new ItemDto ( );
            savedItemDto.setItemName ( savedItem.getItemName ( ) );
            savedItemDto.setDescription ( savedItem.getDescription ( ) );
            savedItemDto.setSalesPrice ( savedItem.getSalesPrice ( ) );
            savedItemDto.setPurchasePrice ( savedItem.getPurchasePrice ( ) );
            savedItemDto.setQuantity ( savedItem.getQuantity ( ) );

            // Add the saved ItemDto to the list
            savedItems.add ( savedItemDto );
        }

        // Return the list of saved ItemDtos
        return savedItems;
    }
}

//    public List<ItemDto> getItemsByCategory(String categoryName) {
//        Category category = categoryRepository.findByName(categoryName)
//                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
//
//        return itemRepository.findByCategory(category).stream()
//                .map(itemMapper::itemToItemDto)
//                .collect(Collectors.toList());
//    }
//
//    public Optional<ItemDto> getItemById(Long itemId) {
//        return itemRepository.findById(itemId)
//                .map(itemMapper::itemToItemDto);
//    }

