package org.nepalimarket.nepalimarketproproject.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = false)
public class SpringSecurityConfig {


    @Bean
    public static PasswordEncoder passwordEncoder ( ) {
        return new BCryptPasswordEncoder ( );
    }

    @Bean
    public SecurityFilterChain securityFilterChain ( HttpSecurity http ) throws Exception {
        http
                .httpBasic ( httpSecurityHttpBasicConfigurer -> {
                } )
//                .and()
                .authorizeHttpRequests ( authorizationManagerRequestMatcherRegistry
                        -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers (
                                new AntPathRequestMatcher ( "/api/users/register" ),
                                new AntPathRequestMatcher ( "/swagger-ui/**" )
                        ).permitAll ( )
//                        .requestMatchers(
//                                new AntPathRequestMatcher("/api/customer/**")
//
//                        ).hasAnyAuthority ("CUSTOMER", "ADMIN")
                        .anyRequest ( ).authenticated ( ) )
                .cors ( AbstractHttpConfigurer::disable )
                .csrf ( AbstractHttpConfigurer::disable )
                .formLogin ( AbstractHttpConfigurer::disable );

        return http.build ( );
    }



//    @Bean
//    public SecurityFilterChain securityFilterChain ( HttpSecurity http ) throws Exception {
//        return http
//                //.httpBasic ( httpSecurityHttpBasicConfigurer -> {} )
//
//                .authorizeHttpRequests ( httpRequest -> {
//                    httpRequest
//                            .requestMatchers ( "/api/users/register", "/swagger-ui/index.html" ).permitAll ( )
//                            .requestMatchers ( "/public/home" ).hasAnyRole ("ADMIN","CUSTOMER" )
//                            .anyRequest ().authenticated ();
//
//                })
//                //.requestMatchers ("/api/**").hasAnyRole ( "ADMIN","CUSTOMER" )
//
//
//                .csrf ( AbstractHttpConfigurer::disable )
//                .cors ( AbstractHttpConfigurer::disable )
//                .formLogin ( AbstractHttpConfigurer::disable )
//                .build ( );
//
//    }

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
}
