package com.toel.controller.auth;

// import java.lang.foreign.Linker.Option;
import java.util.ArrayList;
import java.util.Date;
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
import com.toel.service.InfobipService;
import com.toel.service.auth.JwtService;
import com.toel.service.auth.OtpService;

import jakarta.validation.Valid;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.toel.dto.AuthRequestDTO;
import com.toel.dto.JwtResponseDTO;
import com.toel.dto.PermissionDTO;
import com.toel.dto.Api.ApiResponse;
import com.toel.dto.admin.request.Account.Request_AccountCreateOTP;
import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
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
    InfobipService infobipService;
    @Autowired
    private OtpService otpService;
    @Autowired
    ServiceToel serviceToel;
    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String avatarURL = "https://firebasestorage.googleapis.com/v0/b/ebookstore-4fbb3.appspot.com/o/1_W35QUSvGpcLuxPo3SRTH4w.png?alt=media";

    public static Map<String, String> map = new HashMap<>();

    /**
     * @param authRequestDTO
     *  code 1000: đăng nhập thành công!
     *  code 1001: tài khoản không tồn tại!
     *  code 1002: tài khoản không tồn tại!
     *  code 1003:1004 lỗi đăng ký:
     * @return
     */
    @PostMapping("/api/v1/login")
    public ApiResponse<?> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Account ACCOUNTIgnoreCase = accountRepository.findByUsername(authRequestDTO.getUsername());
            if (ACCOUNTIgnoreCase == null) {
                return ApiResponse.<String>build().code(1001).message("Tài khoản không tồn tại!").result(null);
            }
            if (!ACCOUNTIgnoreCase.isStatus()) {
                return ApiResponse.<String>build().code(1001).message("Tài khoản đã bị khóa!").result(null);
            }
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
                return ApiResponse.<JwtResponseDTO>build().code(1000).message("Đăng nhập thành công!")
                        .result(JwtResponseDTO.builder()
                                .accessToken(token)
                                .username(account.getUsername())
                                .id_account(account.getId())
                                .avatar(account.getAvatar())
                                .roles(role.getName())
                                .Permission(dtos)
                                .fullname(account.getFullname())
                                .avatar(account.getAvatar())
                                .build());
            } else {
                throw new UsernameNotFoundException("Invalid user request..!!");
            }
        } catch (BadCredentialsException e) {
            return ApiResponse.<String>build().code(1002).message("Sai mật khẩu!").result(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ApiResponse.<String>build().code(1003).message("Lỗi đăng nhập!").result(e.getMessage());
        } catch (AuthenticationException e) {
            return ApiResponse.<String>build().code(1004).message("Lỗi đăng nhập!").result(e.getMessage());
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
            Role role = account.getRole();
            List<RolePermission> permissions = rolesPermissionRepository.findByRole(role); // Ensure this retrieves
            List<PermissionDTO> dtos = permissions.stream()
                    .map(pr -> new PermissionDTO(
                            pr.getId(),
                            pr.getPermission().getDescription(),
                            pr.getPermission().getCotSlug()))
                    .collect(Collectors.toList());      // Map<String, Object> map = new HashMap<>(); // Infor mation to include in the
            String token = jwtService.GenerateToken(account.getUsername(), map);
            System.out.println("Generated Token: " + token); // Debugging log
            return ResponseEntity.ok(JwtResponseDTO.builder()            // Return the JWT response
                    .accessToken(token)
                    .username(account.getUsername())
                    .id_account(account.getId())
                    .avatar(null)
                    .roles(role.getName())
                    .Permission(dtos)
                    .fullname(account.getFullname())
                    .avatar(account.getAvatar())
                    .build());   // return ResponseEntity.ok(newToken);
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
        } else {        // return ResponseEntity.ok("Chưa đăng ký tài khoản!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("tài khoản email chưa đăng ký!");
        }
    }

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
            account.setCreateAt(new Date());
            account.setStatus(true);
            account.setAvatar(avatarURL);
            String encryptedPassword = passwordEncoder.encode(entity.getPassword());
            account.setPassword(encryptedPassword);
            accountRepository.save(account);
            return ResponseEntity.ok().body("Đăng ký thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    @PostMapping("/api/v2/user/register") // nhập otp //them moth phân biệt là phone hay email
    public ApiResponse<?> registerAccountV2(@RequestBody Account entity, @RequestParam String otp) {
        try {
            if (accountRepository.existsByUsername(entity.getUsername())) {  // Check if username already exists
                throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Username ");
            }
            if (accountRepository.existsByEmail(entity.getEmail())) {     // Check if email already exists
                throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Email ");
            }
            boolean isValidOtp = otpService.verifyOtp(entity.getEmail(), otp);
            if (!isValidOtp) {            // Verify OTP
                throw new AppException(ErrorCode.OBJECT_NOT_FOUND, "Invalid OTP");
            }
            Role role = roleRepository.findById(4)          // Fetch default role for the user
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Role not found"));
            Account account = new Account();
            account.setRole(role);
            account.setUsername(entity.getUsername());
            account.setFullname(entity.getFullname());
            account.setPhone(entity.getPhone());
            account.setEmail(entity.getEmail());
            account.setCreateAt(new Date());
            account.setStatus(true);
            account.setAvatar(avatarURL);
            account.setPassword(passwordEncoder.encode(entity.getPassword()));
            accountRepository.save(account);
       
            emailService.push(     // Send welcome email
                    entity.getEmail(),
                    "Welcome to Toel Shop!",
                    EmailTemplateType.WELCOME,
                    entity.getFullname());
            return ApiResponse.<String>build()
                    .code(200)
                    .message("Registration successful!")
                    .result("ok");
        } catch (AppException e) {   // Return error response for known exceptions
            return ApiResponse.<String>build()
                    .code(400)
                    .message(e.getMessage())
                    .result("error");
        } catch (Exception e) {     // Handle unexpected exceptions
            return ApiResponse.<String>build()
                    .code(500)
                    .message("An unexpected error occurred: " + e.getMessage())
                    .result("error");
        }
    }

    @PostMapping("/api/v1/user/send-otpe")
    public ApiResponse<?> sendOtp(@RequestBody @Valid Request_AccountCreateOTP entity) {
        // String otp = otpService.generateOtp(identifier); // Tạo OTP
        // otpService.saveOtp(identifier, otp); // Lưu OTP và thời gian hết hạn
        if (accountRepository.existsByUsername(entity.getUsername())) {
            throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Tên tài khoản");
        }
        // Check if email already exists
        if (accountRepository.existsByEmail(entity.getEmail())) {
            throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Email");
        }
        if (accountRepository.existsByPhoneIgnoreCase(entity.getEmail())) {
            throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Số điện thoại");
        }
        // if (accountRepository.existsBySh(entity.getEmail())) {
        // throw new AppException(ErrorCode.OBJECT_ALREADY_EXISTS, "Số điện thoại");
        // }

        if ("email".equalsIgnoreCase(entity.getMethod())) {
            String otp = otpService.generateOtp1(entity.getEmail());
            String hashOTP = serviceToel.hashPassword(otp);
            System.out.println("otp nè: " + otp + " hashOTP " + hashOTP + " , mail " + entity.getEmail());
            emailService.push(entity.getEmail(), "Đăng ký tài khoản", EmailTemplateType.DANGKYV3, otp,
                    entity.getFullname()); // DANGKYTAIKHOAN
            return ApiResponse.<String>build().message("OTP đã được gửi qua email.").result(otp);
        } else if ("phone".equalsIgnoreCase(entity.getMethod())) {
            String otp = otpService.generateOtp1(convertPhoneNumber(entity.getPhone()));
            try {
                infobipService.sendSMS(convertPhoneNumber(entity.getPhone()), otp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ApiResponse.<String>build().message("OTP đã được gửi qua phone.").result(otp);
        } else {
            return ApiResponse.build().message("methor không xác định vui lòng kiểm tra lại!.");
        }
    }

    @GetMapping("/api/v1/user/testphone/{phone}")
    public String test(@PathVariable String phone){
        // String phone =
        return  convertPhoneNumber(phone);
    }


    public static String convertPhoneNumber(String phoneNumber) {
        // Kiểm tra nếu số điện thoại bắt đầu với '0'
        if (phoneNumber != null && phoneNumber.startsWith("0")) {
            // Cắt '0' đầu tiên và thay thế bằng +84
            return "84" + phoneNumber.substring(1);
        }
        // Nếu số điện thoại không bắt đầu bằng '0', trả về chính nó
        return  phoneNumber;
    }

    @PostMapping("/api/v2/user/register_1/{otp}")
    public ResponseEntity<?> postMethodName(@PathVariable String otp,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("phone") String phone,
            @RequestParam("fullname") String fullname,
            @RequestParam("password") String password) {
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

            e.printStackTrace();
            return ResponseEntity.ok().body(e);
        }

    }

}
