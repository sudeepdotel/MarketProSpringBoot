package org.nepalimarket.nepalimarketproproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.entity.Address;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.mapper.UserInfoMapper;
import org.nepalimarket.nepalimarketproproject.repository.AddressRepository;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith ( MockitoJUnitRunner.class )
class UserServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserInfoMapper userInfoMapper;


    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        UserInfoRequestDto requestDto = new UserInfoRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("testPassword");
        requestDto.setFullName ( "Test" );
        requestDto.setPhone ("1234567890");

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@example.com");
        userInfo.setPassword("encodedPassword");
        userInfo.setFullName ( "Test" );
        userInfo.setPhone ( "1234567890" );// Replace with actual encoded password

        // Dummy Address
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setZipCode("12345");

        userInfo.setAddress(address);

        // Dummy Roles
        Set<UserRole> roles = new HashSet<> ();
        roles.add(UserRole.ADMIN);
        userInfo.setRole(roles);

        when(userInfoMapper.userInfoRequestDtoToUserInfo(requestDto)).thenReturn(userInfo);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
    }

    @Test
    void testRegisterUser_Success() {
        // Mock dependencies
        UserInfoRequestDto requestDto = new UserInfoRequestDto(); // Provide necessary data
        UserInfo userInfo = new UserInfo(); // Provide necessary data
        when(userInfoMapper.userInfoRequestDtoToUserInfo(requestDto)).thenReturn(userInfo);
        when(userInfoRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(addressRepository.findByStreetAndCityAndStateAndZipCode(any(), any(), any(), any())).thenReturn(Optional.empty());
        when(addressRepository.save(any())).thenReturn(new Address()); // Mock address saving

        // Test
        Optional<String> result = userService.registerUser(requestDto);

        System.out.println ("result for test :" + result );
        // Verify
        assertTrue(result.isPresent());
        assertEquals("Success", result.get());

        // Additional check to handle null savedUser
        verify(userInfo, times(1)).setRole(any()); // Ensure setRole is called at least once
        verify(userInfoRepository, times(1)).save(userInfo);
    }


    @Test
    void testRegisterUser_UserAlreadyPresent() {
        // Mock dependencies
        UserInfoRequestDto requestDto = new UserInfoRequestDto(); // Provide necessary data
        UserInfo userInfo = new UserInfo(); // Provide necessary data
        when(userInfoMapper.userInfoRequestDtoToUserInfo(requestDto)).thenReturn(userInfo);
        when(userInfoRepository.existsByEmail(any())).thenReturn(true);
        // Test
        Optional<String> result = userService.registerUser(requestDto);
        // Verify
        assertTrue(result.isPresent());
        assertEquals("User already present", result.get());

    }
}

