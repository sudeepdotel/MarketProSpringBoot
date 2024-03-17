package org.nepalimarket.nepalimarketproproject.configuration;

import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserToUserDetails implements UserDetails {


    private final String userName;
    private final String password;
    private final Set<UserRole> roles;

    public UserToUserDetails ( UserInfo userInfo ) {

        this.userName = userInfo.getEmail ( );
        this.password = userInfo.getPassword ( );
        this.roles = userInfo.getRole ( );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities ( ) {
        List<GrantedAuthority> authorities = new ArrayList<> ( );
        for (UserRole role : this.roles) {
            authorities.add ( new SimpleGrantedAuthority ( role.getRoleName ( ) ) );


            // If you also want to add the roles obtained from UserRole.fromString
            try {
                UserRole mappedRole = UserRole.fromString ( role.getRoleName ( ) );
                authorities.add ( new SimpleGrantedAuthority ( mappedRole.getRoleName ( ) ) );
            } catch (IllegalArgumentException ignored) {
                // Handle the case where UserRole.fromString throws an exception (invalid role)
            }
        }
        return authorities;
    }

    @Override
    public String getPassword ( ) {
        return password;
    }

    @Override
    public String getUsername ( ) {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isAccountNonLocked ( ) {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isEnabled ( ) {
        return true;
    }
}
