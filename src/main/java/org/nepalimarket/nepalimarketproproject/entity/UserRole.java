package org.nepalimarket.nepalimarketproproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN ( "ROLE_ADMIN" ),
    CUSTOMER ( "ROLE_CUSTOMER" ),
    VENDOR ( "ROLE_VENDOR" ),
    STAFF ( "ROLE_STAFF" );

    private final String roleName;


    public static UserRole fromString ( String role ) {
        for (UserRole userRole : UserRole.values ( )) {
            if (userRole.name ( ).equalsIgnoreCase ( role )) {
                return userRole;
            }
        }
        throw new IllegalArgumentException ( "Invalid role: " + role );
    }


}
