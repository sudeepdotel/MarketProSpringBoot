package org.nepalimarket.nepalimarketproproject.configuration;

import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.nepalimarket.nepalimarketproproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUserDetails implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public CustomerUserDetails ( UserInfoRepository userInfoRepository ) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByEmail(username)
                .orElseThrow (() -> new UsernameNotFoundException ( "User not found with username::" + username ));
        return null;
    }
}
