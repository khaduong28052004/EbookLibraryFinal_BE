package com.toel.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.toel.service.auth.CustomAccessDeniedHandler;
import com.toel.service.auth.CustomAuthenticationEntryPoint;
import com.toel.service.auth.UserService;

// import com.kot.auth.service.userService;
// .service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    // authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Public access for login, home, and some user routes

                        .requestMatchers("/api/v1/login", "/api/v1/**", "/api/v2/**","/api/v1/otp/**","/user/loginGoogle").permitAll()

                        // Product permissions
                        .requestMatchers("/api/v1/product/create").hasRole("CREATE_PRODUCT")
                        .requestMatchers("/api/v1/product/update").hasRole("UPDATE_PRODUCT")
                        .requestMatchers("/api/v1/product/delete").hasRole("DELETE_PRODUCT")
                        .requestMatchers("/api/v1/product/read").hasRole("READ_PRODUCT")
                        .requestMatchers("/api/v1/product/update_status").hasRole("UPDATE_STATUS_PRODUCT")

                        // FlashSale permissions
                        .requestMatchers("/api/v1/flashsale/create").hasRole("CREATE_FLASHSALE")
                        .requestMatchers("/api/v1/flashsale/update").hasRole("UPDATE_FLASHSALE")
                        .requestMatchers("/api/v1/flashsale/delete").hasRole("DELETE_FLASHSALE")
                        .requestMatchers("/api/v1/flashsale/read").hasRole("READ_FLASHSALE")

                        // DiscountRate permissions
                        .requestMatchers("/api/v1/discountrate/create").hasRole("CREATE_DISCOUNTRATE")
                        .requestMatchers("/api/v1/discountrate/read").hasRole("READ_DISCOUNTRATE")

                        // Voucher permissions
                        .requestMatchers("/api/v1/voucher/create").hasRole("CREATE_VOUCHER")
                        .requestMatchers("/api/v1/voucher/update").hasRole("UPDATE_VOUCHER")
                        .requestMatchers("/api/v1/voucher/delete").hasRole("DELETE_VOUCHER")
                        .requestMatchers("/api/v1/voucher/read").hasRole("READ_VOUCHER")
                        .requestMatchers("/api/v1/voucher/update_status").hasRole("UPDATE_STATUS_VOUCHER")

                        // Account permissions
                        .requestMatchers("/api/v1/account/create").hasRole("CREATE_ACCOUNT")
                        .requestMatchers("/api/v1/account/update").hasRole("UPDATE_ACCOUNT")
                        .requestMatchers("/api/v1/account/delete").hasRole("DELETE_ACCOUNT")
                        .requestMatchers("/api/v1/account/read").hasRole("READ_ACCOUNT")
                        .requestMatchers("/api/v1/account/update_status").hasRole("UPDATE_STATUS_ACCOUNT")

                        // RolesPermission permissions
                        .requestMatchers("/api/v1/roles_permission/read").hasRole("READ_ROLES_PERMISSION")
                        .requestMatchers("/api/v1/roles_permission/update").hasRole("UPDATE_ROLES_PERMISSION")
                        .requestMatchers("/api/v1/roles_permission/delete").hasRole("DELETE_ROLES_PERMISSION")

                        // Category permissions
                        .requestMatchers("/api/v1/category/read").hasRole("READ_CATEGORY")
                        .requestMatchers("/api/v1/category/delete").hasRole("DELETE_CATEGORY")

                        // Bill permissions
                        .requestMatchers("/api/v1/bill/read").hasRole("READ_BILL")
                        .requestMatchers("/api/v1/bill/delete").hasRole("DELETE_BILL")

                        // Evaluate permissions
                        .requestMatchers("/api/v1/evaluate/read").hasRole("READ_EVALUATE")
                        .requestMatchers("/api/v1/evaluate/delete").hasRole("DELETE_EVALUATE")
                        
                        

                        // Cart and other endpoints for both admin and user roles
                        .requestMatchers("/api/v1/cart/**").hasAnyRole("ADMIN", "USER"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(form -> form.loginPage("http://localhost:3000/login").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("http://localhost:3000").deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    // CorsConfiguration configuration = new CorsConfiguration();
    // configuration.addAllowedOrigin("*"); // Cho phép tất cả các nguồn (origins)
    // // configuration.addAllowedMethod("GET");
    // // configuration.addAllowedMethod("POST");
    // // configuration.addAllowedMethod("PUT");
    // // configuration.addAllowedMethod("DELETE");
    // // configuration.addAllowedMethod("PATCH");
    // // configuration.addAllowedMethod("OPTIONS");
    // // // Cho phép tất cả các header
    // // configuration.addAllowedHeader("*");
    // // Nếu bạn muốn cho phép credentials (cookie, authentication), thì đừng dùng
    // `*`
    // // trong allowedOrigins
    // configuration.setAllowCredentials(true);
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", configuration);
    // return source;
    // }
}
