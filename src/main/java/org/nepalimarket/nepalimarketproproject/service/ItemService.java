package org.nepalimarket.nepalimarketproproject.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.access.UnknownAccessTypeException;
import org.nepalimarket.nepalimarketproproject.dto.ItemDto;
import org.nepalimarket.nepalimarketproproject.dto.ItemResponseDto;
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
@Slf4j
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


    public List<ItemDto> addItem(SaveItemRequestDto requestDto, String loggedInUsername) {
        // Check if the logged-in user has the Admin role
        boolean isAdmin = userRepository.existsByEmailAndRoleRoleName(loggedInUsername, UserRole.ADMIN);

        if (!isAdmin) {
            // Throw an exception if the user doesn't have admin role
            throw new UnknownAccessTypeException("You don't have permission to add items.");
        }

        // Find or create a category based on the request
        Category category = categoryRepository.findByName(requestDto.getCategoryName())
                .orElseGet(() -> categoryRepository.save(new Category(requestDto.getCategoryName())));

        // List to store the saved ItemDtos
        List<ItemDto> savedItems = new ArrayList<>();

        // Loop through the items in the request and save or update them
        for (ItemDto itemDto : requestDto.getItems()) {
            // Check if the item already exists in the category
            Item existingItem = itemRepository.findByCategoryAndItemName(category, itemDto.getItemName());

            if (existingItem != null) {
                // If the item exists, update its quantity
                existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity());
                itemRepository.save(existingItem);

                // Create a new ItemDto and set its properties from the updated Item entity
                ItemDto updatedItemDto = new ItemDto();
                updatedItemDto.setItemName(existingItem.getItemName());
                updatedItemDto.setDescription(existingItem.getDescription());
                updatedItemDto.setSalesPrice(existingItem.getSalesPrice());
                updatedItemDto.setPurchasePrice(existingItem.getPurchasePrice());
                updatedItemDto.setQuantity(existingItem.getQuantity());

                // Add the updated ItemDto to the list
                savedItems.add(updatedItemDto);
            } else {
                // If the item does not exist, create a new one
                Item newItem = new Item();
                newItem.setItemName(itemDto.getItemName());
                newItem.setDescription(itemDto.getDescription());
                newItem.setSalesPrice(itemDto.getSalesPrice());
                newItem.setPurchasePrice(itemDto.getPurchasePrice());
                newItem.setQuantity(itemDto.getQuantity());
                newItem.setDateOfEntry(new Date());
                newItem.setCategory(category);

                // Save the new item to the database
                Item savedItem = itemRepository.save(newItem);

                // Create a new ItemDto and set its properties from the saved Item entity
                ItemDto savedItemDto = new ItemDto();
                savedItemDto.setItemName(savedItem.getItemName());
                savedItemDto.setDescription(savedItem.getDescription());
                savedItemDto.setSalesPrice(savedItem.getSalesPrice());
                savedItemDto.setPurchasePrice(savedItem.getPurchasePrice());
                savedItemDto.setQuantity(savedItem.getQuantity());

                // Add the saved ItemDto to the list
                savedItems.add(savedItemDto);
            }
        }

        // Return the list of saved ItemDtos
        return savedItems;
    }

    /**
     *
     * @param loggedUser
     * @return Items
     */
    public List<ItemResponseDto> getAllItems ( String loggedUser ) {
        //Todo: if item is in stock then fetch all the items else don't show items
        List<Item> retrievedItems = itemRepository.findAll ();

        List<Item> checkedQuantity = retrievedItems.stream ().filter ( quantityCheck -> quantityCheck.getQuantity ()>1 )
                .toList ( );

        return checkedQuantity.stream ()
                .map ( itemMapper::itemToItemResponseDto )
                .collect( Collectors.toList());
    }


    public List<ItemDto> getItemsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

        return itemRepository.findByCategory(category).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    public Optional<ItemDto> getItemById( Long itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::itemToItemDto);
    }

}