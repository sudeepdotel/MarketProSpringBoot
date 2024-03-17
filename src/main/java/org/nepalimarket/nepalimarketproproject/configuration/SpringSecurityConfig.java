package org.nepalimarket.nepalimarketproproject.configuration;


import org.nepalimarket.nepalimarketproproject.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
public class SpringSecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public static PasswordEncoder passwordEncoder ( ) {
        return new BCryptPasswordEncoder ( );
    }

    @Bean
    public SecurityFilterChain filterChain ( HttpSecurity http ) throws Exception {
        http

                .httpBasic ( httpSecurityHttpBasicConfigurer -> {
                } )
                .authorizeHttpRequests ( authorizationManagerRequestMatcherRegistry
                        -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers (
                                new AntPathRequestMatcher ( "/api/users/register" ),
                                new AntPathRequestMatcher ( "/api/users/generateToken" ),
                                new AntPathRequestMatcher ( "/swagger-ui/**" ),
                                new AntPathRequestMatcher ( "/items/getAllItems " ),
                                new AntPathRequestMatcher ( "/items/add " )

                        ).permitAll ( )
                        .requestMatchers (
                                new AntPathRequestMatcher ( HttpHeaders.ALLOW ) )
                        .permitAll ( )
                        .anyRequest ( ).authenticated ( ) )
                .authenticationProvider ( this.authenticationProvider ( ) )
                .sessionManagement ( sessions -> sessions.sessionCreationPolicy ( SessionCreationPolicy.STATELESS ) )
                .addFilterBefore ( jwtFilter, UsernamePasswordAuthenticationFilter.class )
                .csrf ( AbstractHttpConfigurer::disable )
                //.cors ( AbstractHttpConfigurer::disable )
                .formLogin ( AbstractHttpConfigurer::disable );


        return http.build ( );
    }

    @Bean
    public UserDetailsService userDetailsService ( ) {
        return new CustomUserDetailsService ( );
    }

    @Bean
    public AuthenticationProvider authenticationProvider ( ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider ( );
        provider.setUserDetailsService ( userDetailsService ( ) );
        provider.setPasswordEncoder ( passwordEncoder ( ) );
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager ( AuthenticationConfiguration configuration ) throws Exception {
        return configuration.getAuthenticationManager ( );

    }
}


