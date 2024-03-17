package org.nepalimarket.nepalimarketproproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
public class HomeController {


    @GetMapping("/home")
    public ResponseEntity<String> homePage ( @RequestParam(value = "name") String name ) {
        log.info ( "successful !!" );
        return ResponseEntity.ok ( "Welcome " + name + " to the Home Page!!" );
    }
}
