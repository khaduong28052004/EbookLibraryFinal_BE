package com.toel.service.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toel.model.Account;
import com.toel.model.Permission;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.AccountRepository;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RolePermissionRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PermissionRepository rermissionRepository;
    @Autowired
    RolePermissionRepository rolesPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        String password = account.getPassword();
        Role role = account.getRole();
        List<RolePermission> permissions = rolesPermissionRepository.findByRole(role);
        Set<GrantedAuthority> authorities = new HashSet<>();
        // // Add role authority
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        // Add permission authorities
        permissions.forEach(permission -> {
            authorities.add(new SimpleGrantedAuthority(
                    "ROLE_" + permission.getPermission().getCotSlug()));
        });
        // permissions.forEach(permission -> {
        // authorities.add(new SimpleGrantedAuthority(
        // "ROLE_" + role.getName() + "_" + permission.getPermission().getCotSlug()));
        // });
        System.out.println(authorities);
        return new User(username, password, authorities);
    }

}
