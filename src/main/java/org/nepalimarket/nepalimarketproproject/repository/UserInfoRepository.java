package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByEmail ( String username );
    void deleteUserInfoByEmail(String email);

    boolean existsByEmail ( String email );
}
