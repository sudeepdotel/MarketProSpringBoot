package org.nepalimarket.nepalimarketproproject.controller;

import org.nepalimarket.nepalimarketproproject.dto.ItemDto;
import org.nepalimarket.nepalimarketproproject.dto.SaveItemRequestDto;
import org.nepalimarket.nepalimarketproproject.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/add")
    public ResponseEntity<List<ItemDto>> addItem(@Validated @RequestBody SaveItemRequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String loggedInUsername = userDetails.getUsername();
        List<ItemDto> addedItems = itemService.addItem(requestDto, loggedInUsername);

        if (!addedItems.isEmpty()) {
            return new ResponseEntity<>(addedItems, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
