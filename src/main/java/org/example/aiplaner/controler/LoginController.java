package org.example.aiplaner.controler;

import org.example.aiplaner.DTO.LogMessage;
import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.Service.LoginService;
import org.example.aiplaner.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public LoginController(LoginService loginService,JwtUtils jwtUtils) {
        this.loginService = loginService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping ("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LogMessage logMessage){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        if(Objects.equals(userEntity.getUsername(), "") || Objects.equals(userEntity.getPassword(), "")){
            return ResponseEntity.status(401).body(Map.of("message","账号或密码已经存在"));
        }
        Optional<UserEntity> userEntity2= Optional.ofNullable(loginService.login(logMessage.getUsername(), logMessage.getPassword()));
        if(userEntity2.isPresent()){
            return ResponseEntity.status(200).body(Map.of("status",true,"id",userEntity2.get().getId(),"token",jwtUtils.generateToken(userEntity2.get())));
        }
        else{
            return ResponseEntity.status(401).body(Map.of("message","密码或账号错误"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody LogMessage logMessage) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        userEntity.setEmail(logMessage.getEmail());
        userEntity.setName(logMessage.getName());
        if(Objects.equals(userEntity.getUsername(), "") || Objects.equals(userEntity.getPassword(), "")){
            return ResponseEntity.status(401).body(Map.of("message","账号或密码不能为空"));
        }
        System.out.println(userEntity.getEmail());
        if(loginService.Register(userEntity)){
            return ResponseEntity.status(200).body(Map.of("status",true));
        }
        else{
            return ResponseEntity.status(401).body(Map.of("message","账号或密码已经存在"));
        }
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody LogMessage logMessage){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(logMessage.getUsername());
        userEntity.setPassword(logMessage.getPassword());
        userEntity.setEmail(logMessage.getEmail());
        String code = logMessage.getCode();

        Optional<UserEntity> user=Optional.ofNullable(loginService.checkCode(userEntity,code)) ;
        if(user.isPresent()){
             UserEntity userEntity1=loginService.login(user.get().getUsername(),user.get().getPassword());
             return ResponseEntity.status(200).body(Map.of("status",true, "token",jwtUtils.generateToken(userEntity1)));
        }
        else{
            return null;
        }
    }


}
