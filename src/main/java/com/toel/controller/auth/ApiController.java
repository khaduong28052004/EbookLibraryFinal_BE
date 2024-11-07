package com.toel.controller.auth;

import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.toel.service.auth.JwtService;
import com.toel.dto.AuthRequestDTO;
import com.toel.dto.JwtResponseDTO;
import com.toel.dto.OtpDTO;
import com.toel.dto.PermissionDTO;
import com.toel.model.Account;
import com.toel.model.Permission;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.AccountRepository;
// import com.toel.repository.OtpDTORepository;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RoleRepository;
import com.toel.repository.RolePermissionRepository;

// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleDetailRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RolePermissionRepository rolesPermissionRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Map<String, String> map = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Account ACCOUNTIgnoreCase = accountRepository.findByUsername(authRequestDTO.getUsername());
            if (!ACCOUNTIgnoreCase.getUsername().equals(authRequestDTO.getUsername())) {
                map.put("error", "Incorrect username!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            }
            // Authenticate the user using their username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),
                            authRequestDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                Account account = accountRepository.findByUsername(authRequestDTO.getUsername());

                Role role = account.getRole();
                List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves

                List<PermissionDTO> dtos = permissions.stream()
                        .map(pr -> new PermissionDTO(
                                pr.getId(),
                                pr.getPermission().getDescription(),
                                pr.getPermission().getCotSlug()))
                        .collect(Collectors.toList());

                Map<String, Object> map = new HashMap<>(); // Infor mation to include in the JWT
                String token = jwtService.GenerateToken(authRequestDTO.getUsername(), map);
                System.out.println("Generated Token: " + token); // Debugging log

                // Return the JWT response
                return ResponseEntity.ok(JwtResponseDTO.builder()
                        .accessToken(token)
                        .username(account.getUsername())
                        .id_account(account.getId())
                        .avatar(null)
                        .roles(role.getName())
                        .Permission(dtos)
                        .build());
            } else {
                throw new UsernameNotFoundException("Invalid user request..!!");
            }
        } catch (BadCredentialsException e) {
            // Return error message for wrong username or password
            map.put("error", "Incorrect password or username 1");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (UsernameNotFoundException e) {
            map.put("error", "User not found.");// error mk end un
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } catch (AuthenticationException e) {
            map.put("error", "Incorrect password or username 2");// error mk end un
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

    @GetMapping("/product/delete")
    public String getMethodName() {
        return "oke delete product";
    }

    @GetMapping("/flashsale/create")
    public String falshsale() {
        return "oke create flashsale";
    }

    // @Autowired
    // private OtpDTORepository dao;

    // @PostMapping("/user/redis")
    // public OtpDTO save(@RequestBody OtpDTO product) {
    // return dao.save(product);
    // }

    // @GetMapping("/user/redis")
    // public List<OtpDTO> getAllProducts() {
    // return dao.findAll();
    // }

    // @GetMapping("/user/redis/{id}")
    // public OtpDTO findProduct(@PathVariable int id) {
    // return dao.findOtpById(id);
    // }

    // @DeleteMapping("/user/redis/{id}")
    // public String remove(@PathVariable int id) {
    // return dao.deleteOtp(id);
    // }

    // @GetMapping("/admin")
    // public String admin() {
    // return "dao.findOtpById(id)";
    // }

    @PostMapping("/user/token")
    public ResponseEntity<?> giahan(@RequestBody JwtResponseDTO entity) {
        String accessToken = entity.getAccessToken();
        if (!jwtService.isTokenExpired(accessToken)) {
            String username = jwtService.extractUsername(accessToken);
            System.out.println(username);
            Map<String, Object> map = new HashMap<>(); // Information to include in the new JWT
            String newToken = jwtService.GenerateToken(username, map); // Generate a new token
            System.out.println("Generated Token: " + newToken);
            System.out.println("t o ke=====:" + jwtService.isTokenExpired(accessToken));
            return ResponseEntity.ok(newToken);
        } else {
            return ResponseEntity.ok("lỗi");
        }
    }

    @PostMapping("/user/loginGoogle")
    public ResponseEntity<?> AuthGG(@RequestBody Account entity) {
        String email = entity.getEmail();
        Account account = accountRepository.findByEmail(email); // một email một tài khoản
        if (account != null) {
            // List<RoleDetail> listRoleDetail =
            // roleDetailRepository.findByAccount(account.getId());
            List<Role> roles = new ArrayList<>();
            // for (RoleDetail roleDetail : listRoleDetail) {
            // roles.add(roleDetail.getRole());
            // }
            Map<String, Object> map = new HashMap();
            String token = jwtService.GenerateToken(account.getUsername(), map);
            System.out.println("Generated Token: " + token); // Debugging log
            Role role = account.getRole();
            List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves

            List<PermissionDTO> dtos = permissions.stream()
                    .map(pr -> new PermissionDTO(
                            pr.getId(),
                            pr.getPermission().getDescription(),
                            pr.getPermission().getCotSlug()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(token)
                    .username(account.getUsername())
                    .id_account(account.getId())
                    .avatar(account.getAvatar())
                    .roles(role.getName())
                    .Permission(dtos)
                    .build());
        } else {
            // return ResponseEntity.ok("Chưa đăng ký tài khoản!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("tài khoản email chưa đăng ký!");
        }
    }

    @PostMapping("/user/loginGoogle1")
    public ResponseEntity<?> AuthGG(@RequestBody Account entity, @RequestParam String acctoken) {
        String email = entity.getEmail();
        Account account = accountRepository.findByEmail(email);

        if (account != null) {
            // Decode the acctoken
            String decodedToken = decodeAccessToken(acctoken);

            // You might want to verify the token here
            if (!verifyGoogleToken(decodedToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            // Add more claims as needed

            String token = jwtService.GenerateToken(account.getUsername(), claims);
            System.out.println("Generated Token: " + token); // Debugging log

            // List<Permission> permissionList = permissionRepository.findAll();
            // List<PermissionDTO> permissionDTOList = permissionList.stream()
            // .map(this::convertToPermissionDTO)
            // .collect(Collectors.toList());

            // List<Permission> list1List = permissionRepository.findAll();
            // List<PermissionDTO> permissionDTOList = new ArrayList();
            // PermissionDTO per = new PermissionDTO();
            // for (Permission iterable_element : list1List) {
            //     // per.setId(null);
            //     per.getId();
            //     per.getCotSlug();
            //     per.getDescription();
            //     permissionDTOList.add(per);
            // }

            // return ResponseEntity.ok(JwtResponseDTO.builder()
            //         .accessToken(token)
            //         .username(account.getUsername())
            //         .id_account(account.getId())
            //         .avatar(account.getAvatar())
            //         .roles("USER") // You might want to fetch actual roles
            //         .Permission(permissionDTOList)
            //         .build());

            Role role = account.getRole();
            List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves

            List<PermissionDTO> dtos = permissions.stream()
                    .map(pr -> new PermissionDTO(
                            pr.getId(),
                            pr.getPermission().getDescription(),
                            pr.getPermission().getCotSlug()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(token)
                    .username(account.getUsername())
                    .id_account(account.getId())
                    .avatar(account.getAvatar())
                    .roles(role.getName())
                    .Permission(dtos)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email account not registered!");
        }
    }

    private String decodeAccessToken(String acctoken) {
        // Implement the logic to decode the access token
        // This might involve using a library like java-jwt or your custom logic
        // For example:
        // return JWT.decode(acctoken).getClaim("sub").asString();
        // Return the decoded token or relevant information
        return "Decoded token"; // Placeholder
    }

    private boolean verifyGoogleToken(String decodedToken) {
        // Implement the logic to verify the Google token
        // This might involve making a call to Google's tokeninfo endpoint
        // or using Google's client library
        // Return true if the token is valid, false otherwise
        return true; // Placeholder
    }

    // private PermissionDTO convertToPermissionDTO(Permission permission) {
    // return PermissionDTO.builder()
    // .id(permission.getId())
    // .cotSlug(permission.getCotSlug())
    // .description(permission.getDescription())
    // .build();
    // // return ;
    // }

    @PostMapping("/user/register")
    public ResponseEntity<?> RegisterAcoount(@RequestBody Account entity) {
        if(accountRepository.existsByUsername(entity.getUsername())){
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        if(accountRepository.existsByEmail(entity.getEmail())){
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        Account account = new Account();
        Role role = roleRepository.findById(3).orElseThrow(() -> new RuntimeException("Role not found"));
        // RoleDetail roleDetail = new RoleDetail();
        account.setUsername(entity.getUsername());
        account.setEmail(entity.getEmail());
        account.setFullname(entity.getFullname());
        account.setPhone(entity.getPhone());

        String encryptedPassword = passwordEncoder.encode(entity.getPassword());
        account.setPassword(encryptedPassword);
        account.setAvatar("noimg.png");
        account.setRole(role);
        accountRepository.save(account);
        return ResponseEntity.ok().body("Đăng ký thành công!");
    }

}
