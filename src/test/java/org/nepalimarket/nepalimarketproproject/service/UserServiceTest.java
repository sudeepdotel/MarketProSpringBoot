package org.nepalimarket.nepalimarketproproject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.nepalimarket.nepalimarketproproject.dto.AddressDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.entity.Address;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.mapper.UserInfoMapper;
import org.nepalimarket.nepalimarketproproject.repository.AddressRepository;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
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
    }

    @Test
    void testRegisterUser_Success() {
        // Mock dependencies
        UserInfoRequestDto requestDto = new UserInfoRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");
        requestDto.setFullName("Test User");
        requestDto.setPhone("1234567890");

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("123 Test St");
        addressDto.setCity("Test City");
        addressDto.setState("TX");
        addressDto.setZipCode("12345");
        requestDto.setAddress(addressDto);

//        Set<String> userRoles = new HashSet<> (  );
//        userRoles.add ("ADMIN");
//        requestDto.setRole ( userRoles);

         UserInfo userInfo = new UserInfo();
//        userInfo.setEmail(requestDto.getEmail());
//        userInfo.setPassword(requestDto.getPassword());
//        userInfo.setFullName(requestDto.getFullName());
//        userInfo.setPhone(requestDto.getPhone());

        Mockito.when(userInfoMapper.userInfoRequestDtoToUserInfo(requestDto)).thenReturn(userInfo);
        Mockito.when(userInfoRepository.existsByEmail(any())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        Mockito.when(addressRepository.findByStreetAndCityAndStateAndZipCode(
                        addressDto.getStreet(), addressDto.getCity(), addressDto.getState(), addressDto.getZipCode()))
                .thenReturn(Optional.empty());

        Address savedAddress = new Address(); // Creating a mock Address instance
        Mockito.when(addressRepository.save(any())).thenReturn(savedAddress); // Mocking the addressRepository save method

        // Mocking the save method of userInfoRepository to return a mock UserInfo object
        UserInfo savedUser = Mockito.mock(UserInfo.class);
        Mockito.when(userInfoRepository.save(any())).thenReturn(savedUser);

        // Mock the setRole() method on savedUser
       // Mockito.doNothing().when(savedUser).setRole(any());
        // Test
        Optional<String> result = userService.registerUser(requestDto);

        // Verify
        assertTrue(result.isPresent());
        assertEquals("Success", result.get());

        // Additional checks
      //  Mockito.verify(savedUser, Mockito.times(1)).setRole(any());
        Mockito.verify(userInfoRepository, Mockito.times(1)).save(any());
    }



    @Test
    void testRegisterUser_UserAlreadyPresent() {
        // Mock dependencies
        UserInfoRequestDto requestDto = new UserInfoRequestDto(); // Provide necessary data
        UserInfo userInfo = new UserInfo(); // Provide necessary data
        Mockito.when(userInfoMapper.userInfoRequestDtoToUserInfo(requestDto)).thenReturn(userInfo);
        Mockito.when(userInfoRepository.existsByEmail(any())).thenReturn(true);
        // Test
        Optional<String> result = userService.registerUser(requestDto);
        // Verify
        assertTrue(result.isPresent());
        assertEquals("User already present", result.get());

    }
}

