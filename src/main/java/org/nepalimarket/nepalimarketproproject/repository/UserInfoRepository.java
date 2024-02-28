package org.nepalimarket.nepalimarketproproject.repository;

import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByEmail ( String username );
    void deleteUserInfoByEmail(String email);

    boolean existsByEmail ( String email );

    @Query("SELECT COUNT(u) > 0 FROM UserInfo u WHERE u.email = :email AND :roleName MEMBER OF u.role")
    boolean existsByEmailAndRoleRoleName(@Param("email") String email, @Param("roleName") UserRole roleName);
}
