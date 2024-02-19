package org.nepalimarket.nepalimarketproproject.configuration;

import lombok.NoArgsConstructor;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private  UserInfoRepository userInfoRepository;


    @Override
    public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {

        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(username);


        return userInfo.map ( UserToUserDetails::new )
                .orElseThrow (() -> new UsernameNotFoundException ( "User not found with username:: " + username ));
    }
}
