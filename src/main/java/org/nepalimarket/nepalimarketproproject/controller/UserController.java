package org.nepalimarket.nepalimarketproproject.controller;

import jakarta.validation.Valid;
import org.nepalimarket.nepalimarketproproject.dto.AuthRequest;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoResponseDto;
import org.nepalimarket.nepalimarketproproject.service.JwtService;
import org.nepalimarket.nepalimarketproproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser ( @Valid @RequestBody UserInfoRequestDto userInfo ) {
        Map<String, String> response = new HashMap<> ( );

        Optional<String> userInfoOptional = userService.registerUser ( userInfo );

        return userInfoOptional
                .map ( s -> {
                    response.put ( "message", s );
                    return new ResponseEntity<> ( response, HttpStatus.CREATED );
                } )
                .orElseGet ( ( ) -> {
                    response.put ( "message", "Failed to register.... Try again !!" );
                    return new ResponseEntity<> ( response, HttpStatus.BAD_REQUEST );
                } );
    }


    @GetMapping("/{email}")
    public ResponseEntity<UserInfoResponseDto> getUserByEmail ( @PathVariable String email ) {
        Optional<UserInfoResponseDto> userInfo = userService.getUserByEmail ( email );

        return userInfo.map ( ResponseEntity::ok )
                .orElseGet ( ( ) -> ResponseEntity.notFound ( ).build ( ) );
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser ( @PathVariable String email ) {
        if (userService.deleteUserByEmail ( email )) {
            return ResponseEntity.ok ( "User deleted successfully." );
        } else {
            return ResponseEntity.badRequest ( ).body ( "User not found or unable to delete." );
        }
    }

    @PostMapping("/generateToken")
    public String generateToken ( @RequestBody AuthRequest authRequest ) {
        Authentication authentication = authenticationManager.authenticate ( new UsernamePasswordAuthenticationToken ( authRequest.getUserName ( ), authRequest.getPassword ( ) ) );
        if (authentication.isAuthenticated ( )) {
            return jwtService.generateToken ( authRequest.getUserName ( ) );
        } else {
            throw new UsernameNotFoundException ( "Invalid credentials!!" );
        }

    }

}
