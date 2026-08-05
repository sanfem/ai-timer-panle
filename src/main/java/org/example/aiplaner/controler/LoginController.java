package org.example.aiplaner.controler;


import jakarta.servlet.http.Cookie;
import org.example.aiplaner.DTO.LogMessage;
import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.Service.LoginService;
import org.example.aiplaner.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/Login")
public class LoginController {
    private LoginService loginService;
    private JwtUtils jwtUtils;

    @Autowired
    public LoginController(LoginService loginService, JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LogMessage logMessage) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        if (Objects.equals(userEntity.getUsername(), "") || Objects.equals(userEntity.getPassword(), "")) {
            return ResponseEntity.status(401).body(Map.of("message", "账号或密码已经存在"));
        }
        Optional<UserEntity> userEntity2 = Optional.ofNullable(loginService.login(logMessage.getUsername(), logMessage.getPassword()));


        if (userEntity2.isPresent()) {
            // 存到 Cookie（设置 HttpOnly 防止 XSS 攻击）
            ResponseCookie cookie = ResponseCookie.from("token",  jwtUtils.generateToken(userEntity2.get()))  // 名字和值
                    .httpOnly(true)          // 防止 XSS 攻击
                    .secure(false)            // 仅 HTTPS 传输
                    .path("/")               // 对所有路径生效
                    .maxAge(Duration.ofDays(7))  // 7天过期
                    .sameSite("Strict")      // 防止 CSRF 攻击
                    .build();
            return ResponseEntity.status(200).header("Set-Cookie", cookie.toString()).body(Map.of("status", true, "id", userEntity2.get().getId()));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "密码或账号错误"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody LogMessage logMessage) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        userEntity.setEmail(logMessage.getEmail());
        userEntity.setName(logMessage.getName());
        if (Objects.equals(userEntity.getUsername(), "") || Objects.equals(userEntity.getPassword(), "")) {
            return ResponseEntity.status(401).body(Map.of("message", "账号或密码不能为空"));
        }
        System.out.println(userEntity.getEmail());
        if (loginService.Register(userEntity)) {
            return ResponseEntity.status(200).body(Map.of("status", true));
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "账号或密码已经存在"));
        }
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody LogMessage logMessage) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        userEntity.setEmail(logMessage.getEmail());
        String code = logMessage.getCode();

        Optional<UserEntity> user = Optional.ofNullable(loginService.checkCode(userEntity, code));
        if (user.isPresent()) {
            UserEntity userEntity1 = loginService.login(user.get().getUsername(), user.get().getPassword());
            // 存到 Cookie（设置 HttpOnly 防止 XSS 攻击）
            ResponseCookie cookie = ResponseCookie.from("token", jwtUtils.generateToken(userEntity1))  // 名字和值
                    .httpOnly(true)          // 防止 XSS 攻击
                    .secure(false)            // 仅 HTTPS 传输
                    .path("/")               // 对所有路径生效
                    .maxAge(Duration.ofDays(7))  // 7天过期
                    .sameSite("Strict")      // 防止 CSRF 攻击
                    .build();
            return ResponseEntity.status(200).body(Map.of("status", true));
        } else {
            return null;
        }
    }
}
