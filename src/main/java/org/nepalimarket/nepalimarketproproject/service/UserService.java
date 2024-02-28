package org.nepalimarket.nepalimarketproproject.service;


import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.nepalimarket.nepalimarketproproject.configuration.SpringSecurityConfig;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoResponseDto;
import org.nepalimarket.nepalimarketproproject.entity.Address;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.nepalimarket.nepalimarketproproject.exception.MultipleExceptionsOccurredException;
import org.nepalimarket.nepalimarketproproject.mapper.UserInfoMapper;
import org.nepalimarket.nepalimarketproproject.repository.AddressRepository;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final UserInfoMapper userInfoMapper; // Inject the mapper

    @Autowired
    public UserService(UserInfoRepository userInfoRepository, PasswordEncoder passwordEncoder,
                       AddressRepository addressRepository, UserInfoMapper userInfoMapper) {
        this.userInfoRepository = userInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
        this.userInfoMapper = userInfoMapper;
    }

    public Optional<String> registerUser(UserInfoRequestDto userInfoRequestDto) {
        try {
            // Map UserInfoRequestDto to UserInfo
            UserInfo userInfo = userInfoMapper.userInfoRequestDtoToUserInfo(userInfoRequestDto);

            // Check if the user with the given username is already present
            if (userInfoRepository.existsByEmail(userInfo.getEmail ())) {
                log.error("Error registering user: User already present");
                return Optional.of("User already present");
            }

            // Encode password
            String encodedPassword = passwordEncoder.encode(userInfoRequestDto.getPassword());
            userInfo.setPassword(encodedPassword);

            // Check if the address already exists in the database
            if (userInfo.getAddress() != null) {
                Optional<Address> existingAddress = addressRepository.findByStreetAndCityAndStateAndZipCode(
                        userInfo.getAddress().getStreet(),
                        userInfo.getAddress().getCity(),
                        userInfo.getAddress().getState(),
                        userInfo.getAddress().getZipCode()
                );

                if (existingAddress.isPresent()) {
                    // If the address exists, associate the user with the existing address
                    userInfo.setAddress(existingAddress.get());
                } else {
                    // If the address doesn't exist, save the new address to the database
                    Address newAddress = addressRepository.save(userInfo.getAddress());
                    userInfo.setAddress(newAddress);
                }
            }

            // Save user with roles as strings
            UserInfo savedUser = userInfoRepository.save(userInfo);
            // log something or perform additional actions, you can use the Stream API
            savedUser.getRole().stream()
                    .map(UserRole::getRoleName)
                    .collect(Collectors.toSet())
                    .forEach(roleName -> log.info("User " + savedUser.getFullName() + " has role: " + roleName));

            return Optional.of("Success");
        } catch (Exception e) {
            // Handle exceptions - log or throw a custom exception as needed
            log.error("Error registering user: " + e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<UserInfoResponseDto> getUserByEmail(String email){
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail ( email );
        return userInfo.map(userInfoMapper::userInfoToUserInfoResponseDto);
    }

    public boolean deleteUserByEmail(String email) {
        try {
            Optional<UserInfo> userOptional = userInfoRepository.findByEmail (email);
            if (userOptional.isPresent()) {
                userInfoRepository.delete(userOptional.get());
                log.info("User with Email {} deleted successfully.", email);
                return true;
            } else {
                log.warn("User with Email {} not found.", email);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting user with Email {}: {}", email, e.getMessage());
            return false;
        }
    }


}
