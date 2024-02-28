package org.nepalimarket.nepalimarketproproject.controller;

import jakarta.validation.Valid;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoResponseDto;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserInfoRequestDto userInfo) {

        Optional<String> userInfoOptional = userService.registerUser(userInfo);

        return userInfoOptional
                .map ( s -> new ResponseEntity<> ( s, HttpStatus.CREATED ) )
                .orElseGet ( ( ) -> new ResponseEntity<> (
                        "Failed to register.... Try again !!", HttpStatus.BAD_REQUEST
                        ));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserInfoResponseDto> getUserByEmail(@PathVariable String email) {
        Optional<UserInfoResponseDto> userInfo = userService.getUserByEmail(email);

        return userInfo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        if (userService.deleteUserByEmail (email)) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found or unable to delete.");
        }
    }

}
