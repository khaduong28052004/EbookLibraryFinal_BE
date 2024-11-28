package com.toel.controller.auth;

// import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.server.ResponseStatusException;

import com.toel.service.ServiceToel;
import com.toel.service.Email.EmailService;
import com.toel.service.Email.EmailTemplateType;
import com.toel.service.auth.GoogleTokenVerifier;
import com.toel.service.auth.JwtService;
import com.toel.service.auth.OtpService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.toel.dto.AuthRequestDTO;
import com.toel.dto.JwtResponseDTO;
import com.toel.dto.PermissionDTO;
import com.toel.model.Account;
import com.toel.model.Role;
import com.toel.model.RolePermission;
import com.toel.repository.AccountRepository;
// import com.toel.repository.OtpDTORepository;
import com.toel.repository.PermissionRepository;
import com.toel.repository.RoleRepository;
import com.toel.repository.RolePermissionRepository;

// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
// @RequestMapping("/api/v1")
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
    EmailService emailService;

    @Autowired
    private OtpService otpService;
    @Autowired
    ServiceToel serviceToel;
    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Map<String, String> map = new HashMap<>();

    @PostMapping("/api/v1/login")
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
                        .fullname(account.getFullname())
                        .avatar(account.getAvatar())
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

    @GetMapping("/api/v1/product/delete")
    public String getMethodName() {
        return "oke delete product";
    }

    @GetMapping("/api/v1/flashsale/create")
    public String falshsale() {
        return "";
    }

    @GetMapping("/api/v1/re")
    public ResponseEntity<?> renewToken(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        System.out.println(actualToken);
        Map<String, Object> map = new HashMap<>(); // Information to include in the new JWT
        if (jwtService.isTokenExpired(actualToken)) {
            String username = jwtService.extractUsername(actualToken);
            String newToken = jwtService.GenerateToken(username, map);
            return ResponseEntity.ok(newToken);
        }

        return ResponseEntity.noContent().build(); // Không cần gia hạn
    }

    @PostMapping("/api/v1/user/token")
    public ResponseEntity<?> giahan(@RequestBody JwtResponseDTO entity) {
        String accessToken = entity.getAccessToken();
        if (!jwtService.isTokenExpired(accessToken)) {
            String username = jwtService.extractUsername(accessToken);
            Account account = accountRepository.findByUsername(username);
            System.out.println(username);
            Map<String, Object> map = new HashMap<>(); // Information to include in the new JWT
            String newToken = jwtService.GenerateToken(username, map); // Generate a new token
            System.out.println("Generated Token: " + newToken);
            System.out.println("t o ke=====:" + jwtService.isTokenExpired(accessToken));
            //

            Role role = account.getRole();
            List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves

            List<PermissionDTO> dtos = permissions.stream()
                    .map(pr -> new PermissionDTO(
                            pr.getId(),
                            pr.getPermission().getDescription(),
                            pr.getPermission().getCotSlug()))
                    .collect(Collectors.toList());

            // Map<String, Object> map = new HashMap<>(); // Infor mation to include in the
            // JWT
            String token = jwtService.GenerateToken(account.getUsername(), map);
            System.out.println("Generated Token: " + token); // Debugging log

            // Return the JWT response
            return ResponseEntity.ok(JwtResponseDTO.builder()
                    .accessToken(token)
                    .username(account.getUsername())
                    .id_account(account.getId())
                    .avatar(null)
                    .roles(role.getName())
                    .Permission(dtos)
                    .fullname(account.getFullname())
                    .avatar(account.getAvatar())
                    .build());
            // return ResponseEntity.ok(newToken);
        } else {
            return ResponseEntity.badRequest().body("đã hết hạn");
        }
    }

    @PostMapping("/api/v1/user/loginGoogle1")
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

    // @PostMapping("/user/loginGoogle1")
    // public ResponseEntity<?> AuthGG(@RequestBody Account entity, @RequestParam
    // String acctoken) {
    // String email = entity.getEmail();
    // Account account = accountRepository.findByEmail(email);

    // if (account != null) {
    // // Decode the acctoken
    // String decodedToken = decodeAccessToken(acctoken);

    // // You might want to verify the token here
    // if (!verifyGoogleToken(decodedToken)) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google
    // token");
    // }

    // Map<String, Object> claims = new HashMap<>();
    // claims.put("email", email);
    // // Add more claims as needed

    // String token = jwtService.GenerateToken(account.getUsername(), claims);
    // System.out.println("Generated Token: " + token); // Debugging log

    // // List<Permission> permissionList = permissionRepository.findAll();
    // // List<PermissionDTO> permissionDTOList = permissionList.stream()
    // // .map(this::convertToPermissionDTO)
    // // .collect(Collectors.toList());

    // // List<Permission> list1List = permissionRepository.findAll();
    // // List<PermissionDTO> permissionDTOList = new ArrayList();
    // // PermissionDTO per = new PermissionDTO();
    // // for (Permission iterable_element : list1List) {
    // // // per.setId(null);
    // // per.getId();
    // // per.getCotSlug();
    // // per.getDescription();
    // // permissionDTOList.add(per);
    // // }

    // // return ResponseEntity.ok(JwtResponseDTO.builder()
    // // .accessToken(token)
    // // .username(account.getUsername())
    // // .id_account(account.getId())
    // // .avatar(account.getAvatar())
    // // .roles("USER") // You might want to fetch actual roles
    // // .Permission(permissionDTOList)
    // // .build());

    // Role role = account.getRole();
    // List<RolePermission> permissions =
    // rolesPermissionRepository.findByRole(role); // Ensure this retrieves

    // List<PermissionDTO> dtos = permissions.stream()
    // .map(pr -> new PermissionDTO(
    // pr.getId(),
    // pr.getPermission().getDescription(),
    // pr.getPermission().getCotSlug()))
    // .collect(Collectors.toList());

    // return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(token)
    // .username(account.getUsername())
    // .id_account(account.getId())
    // .avatar(account.getAvatar())
    // .roles(role.getName())
    // .Permission(dtos)
    // .build());
    // } else {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email account not
    // registered!");
    // }
    // }

    // private String decodeAccessToken(String acctoken) {
    // // Implement the logic to decode the access token
    // // This might involve using a library like java-jwt or your custom logic
    // // For example:
    // // return JWT.decode(acctoken).getClaim("sub").asString();
    // // Return the decoded token or relevant information
    // return "Decoded token"; // Placeholder
    // }

    // private boolean verifyGoogleToken(String decodedToken) {
    // // Implement the logic to verify the Google token
    // // This might involve making a call to Google's tokeninfo endpoint
    // // or using Google's client library
    // // Return true if the token is valid, false otherwise
    // return true; // Placeholder
    // }

    // // private PermissionDTO convertToPermissionDTO(Permission permission) {
    // // return PermissionDTO.builder()
    // // .id(permission.getId())
    // // .cotSlug(permission.getCotSlug())
    // // .description(permission.getDescription())
    // // .build();
    // // // return ;
    // // }

    // @PostMapping("/user/register")
    // public ResponseEntity<?> RegisterAcoount(@RequestBody Account entity) {
    // if(accountRepository.existsByUsername(entity.getUsername())){
    // return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
    // }
    // if(accountRepository.existsByEmail(entity.getEmail())){
    // return ResponseEntity.badRequest().body("Email đã tồn tại!");
    // }
    // Account account = new Account();
    // Role role = roleRepository.findById(3).orElseThrow(() -> new
    // RuntimeException("Role not found"));
    // // RoleDetail roleDetail = new RoleDetail();
    // account.setUsername(entity.getUsername());
    // account.setEmail(entity.getEmail());
    // account.setFullname(entity.getFullname());
    // account.setPhone(entity.getPhone());

    // String encryptedPassword = passwordEncoder.encode(entity.getPassword());
    // account.setPassword(encryptedPassword);
    // account.setAvatar("noimg.png");
    // account.setRole(role);
    // accountRepository.save(account);
    // emailService.push(account.getEmail(), "Wellcom Toel Shop!",
    // EmailTemplateType.WELCOME,account.getFullname());
    // return ResponseEntity.ok().body("Đăng ký thành công!");
    // }

    @PostMapping("/api/v1/user/loginGoogle")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody Map<String, String> body) {
        Account account = new Account();
        String token = body.get("token");
        System.out.println("Received ID Token: " + token);
        GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyToken(token);
        if (payload != null) {
            // Trích xuất thông tin người dùng từ payload
            String userId = payload.getSubject(); // Unique Google ID
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            account = accountRepository.findByEmail(email);
        }
        if (account != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", account.getEmail());
            String accessToken = jwtService.GenerateToken(account.getUsername(), claims);
            System.out.println("Generated Token: " + accessToken); // Debugging log

            Role role = account.getRole();
            List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves
            List<PermissionDTO> dtos = permissions.stream()
                    .map(pr -> new PermissionDTO(
                            pr.getId(),
                            pr.getPermission().getDescription(),
                            pr.getPermission().getCotSlug()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(accessToken)
                    .username(account.getUsername())
                    .id_account(account.getId())
                    .avatar(null)
                    .roles(role.getName())
                    .Permission(dtos)
                    .fullname(account.getFullname())
                    .avatar(account.getAvatar())
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.FOUND) // Đường dẫn đến trang đăng ký
                    .body("Email account not registered! Please sign up.");
        }
    }

    @PostMapping("/api/v1/user/register")
    public ResponseEntity<?> RegisterAccount(@RequestBody Account entity) {
        if (accountRepository.existsByUsername(entity.getUsername())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        if (accountRepository.existsByEmail(entity.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        Account account = new Account();
        emailService.push(entity.getEmail(), "Welcome Toel Shop!", EmailTemplateType.WELCOME,
                entity.getFullname());

        try {
            Role role = roleRepository.findById(4).orElseThrow(() -> new RuntimeException("Role not found"));
            account.setRole(role);
            account.setUsername(entity.getUsername());
            account.setFullname(entity.getFullname());
            account.setPhone(entity.getPhone());
            account.setEmail(entity.getEmail());
            account.setAvatar("noImage.png");
            String encryptedPassword = passwordEncoder.encode(entity.getPassword());
            account.setPassword(encryptedPassword);
            accountRepository.save(account);
            return ResponseEntity.ok().body("Đăng ký thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // @PostMapping("/user/loginGoogle")
    // public ResponseEntity<?> AuthGG1(@RequestBody String token) {
    // Account account = new Account();
    // GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyToken(token);

    // if (payload != null) {
    // String userId = payload.getSubject(); // ID duy nhất của người dùng Google
    // String email = payload.getEmail(); // Email của người dùngs
    // System.out.println(email);
    // String name = (String) payload.get("name"); // Tên của người dùng
    // // Kiểm tra người dùng trong database hoặc xử lý logic khác ở đây
    // account = accountRepository.findByEmail(email);
    // // return "Login successful!";
    // }

    // if ( account != null) {
    // // Decode the acctoken
    // // String decodedToken = decodeAccessToken(acctoken);
    // // You might want to verify the token here
    // // if (!verifyGoogleToken(token)) {
    // // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google
    // token");
    // // }
    // Map<String, Object> claims = new HashMap<>();
    // claims.put("email", account.getEmail());
    // // Add more claims as needed
    // String accessToken = jwtService.GenerateToken(account.getUsername(), claims);
    // System.out.println("Generated Token: " + accessToken); // Debugging log
    // // List<Permission> permissionList = permissionRepository.findAll();
    // // List<PermissionDTO> permissionDTOList = permissionList.stream()
    // // .map(this::convertToPermissionDTO)
    // // .collect(Collectors.toList());
    // // List<Permission> list1List = permissionRepository.findAll();
    // // List<PermissionDTO> permissionDTOList = new ArrayList();
    // // PermissionDTO per = new PermissionDTO();
    // // for (Permission iterable_element : list1List) {
    // // // per.setId(null);
    // // per.getId();
    // // per.getCotSlug();
    // // per.getDescription();
    // // permissionDTOList.add(per);
    // // }

    // // return ResponseEntity.ok(JwtResponseDTO.builder()
    // // .accessToken(token)
    // // .username(account.getUsername())
    // // .id_account(account.getId())
    // // .avatar(account.getAvatar())
    // // .roles("USER") // You might want to fetch actual roles
    // // .Permission(permissionDTOList)
    // // .build());

    // Role role = new Role();
    // List<RolePermission> permissions =
    // rolesPermissionRepository.findByRole(role); // Ensure this retrieves

    // List<PermissionDTO> dtos = permissions.stream()
    // .map(pr -> new PermissionDTO(
    // pr.getId(),
    // pr.getPermission().getDescription(),
    // pr.getPermission().getCotSlug()))
    // .collect(Collectors.toList());

    // return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(accessToken)
    // .username(account.getUsername())
    // .id_account(account.getId())
    // .avatar(account.getAvatar())
    // .roles("role.getName()")
    // .Permission(dtos)
    // .build());
    // } else {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email account not
    // registered!");
    // }
    // }

    // private String decodeAccessToken(String acctoken) {
    // // Implement the logic to decode the access token
    // // This might involve using a library like java-jwt or your custom logic
    // // For example:
    // // return JWT.decode(acctoken).getClaim("sub").asString();
    // // Return the decoded token or relevant information
    // return "Decoded token"; // Placeholder
    // }

    // private boolean verifyGoogleToken(String decodedToken) {
    // // Implement the logic to verify the Google token
    // // This might involve making a call to Google's tokeninfo endpoint
    // // or using Google's client library
    // // Return true if the token is valid, false otherwise
    // return true; // Placeholder
    // }

    // private PermissionDTO convertToPermissionDTO(Permission permission) {
    // return PermissionDTO.builder()
    // .id(permission.getId())
    // .cotSlug(permission.getCotSlug())
    // .description(permission.getDescription())
    // .build();
    // // return ;
    // }

    @PostMapping("/api/v2/user/register")
    public ResponseEntity<?> RegisterAcoountV2(@RequestBody Account entity) {
        Account account = new Account();
        if (accountRepository.existsByUsername(entity.getUsername())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
        }
        if (accountRepository.existsByEmail(entity.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        } else {
            String encryptedPassword = passwordEncoder.encode(entity.getPassword());
            String otp = otpService.generateOtp(entity.getEmail());
            String hashOTP = serviceToel.hashPassword(otp);
            emailService.push(entity.getEmail(), "Mã otp của bạn", EmailTemplateType.DANGKYV2, entity.getFullname(),
                    entity.getPhone(), entity.getEmail(), entity.getUsername(), encryptedPassword,
                    "http://localhost:8080/api/v2/user/register_1?otp=" + hashOTP);
            return ResponseEntity.ok("OTP generated: " + otp);
        }
    }

    @PostMapping("/api/v2/user/register_1/{otp}")
    public ResponseEntity<?> postMethodName(@PathVariable String otp,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("phone") String phone,
            @RequestParam("fullname") String fullname,
            @RequestParam("password") String password) {
        // TODO: process POST request
        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                Account account = new Account();
                Role role = roleRepository.findById(4).orElseThrow(() -> new RuntimeException("Role not found"));
                account.setUsername(username);
                account.setEmail(email);
                account.setFullname(fullname);
                account.setPhone(phone);
                account.setPassword(password);
                account.setAvatar("noImage.png");
                account.setRole(role);
                accountRepository.save(account);
                return ResponseEntity.ok().body("oke");
            } else {
                return ResponseEntity.ok().body("lỗi");
            }
        } catch (Exception e) {

            // TODO: handle exception
            e.printStackTrace();
            return ResponseEntity.ok().body(e);
        }

    }

}
