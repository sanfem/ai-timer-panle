package org.example.aiplaner.Service;

import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.dao.Userdao;
import org.example.aiplaner.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class LoginService {
    private Userdao userdao;
    private JavaMailSender mailSender;
    private StringRedisTemplate redisTemplate;



    @Autowired
    public LoginService(Userdao userdao, JavaMailSender mailSender, StringRedisTemplate redisTemplate) {
        this.userdao = userdao;
        this.mailSender=mailSender;
        this.redisTemplate=redisTemplate;
    }

    public UserEntity login(String username, String password){
        Optional<UserEntity> userEntity = userdao.findByUsername(username);
        if(userEntity.isPresent()){
            if(password.equals(userEntity.get().getPassword())){
                return userEntity.get();
            }
        }
        return null;
    }

    public boolean Register(UserEntity userEntity){
         Optional<UserEntity> userEntity1 = userdao.findByUsername(userEntity.getUsername());
         if(userEntity1.isPresent()){
             return false;  //如果有相同用户，返回空值
         }
         else{
             CreateCode(userEntity.getEmail());
             return true;
         }
    }

    public UserEntity checkCode(UserEntity user,String code){
        String realCode = redisTemplate.opsForValue().get("Code:" + user.getEmail());
        if (realCode == null) {
            throw new RuntimeException("验证码已过期");
        }
        if (!realCode.equals(code)) {
            throw new RuntimeException("验证码错误");
        }
        // 验证完删除
        redisTemplate.delete("Code:" + user.getEmail());
        userdao.save(user);
        return login(user.getUsername(), user.getPassword());
    }

    private void CreateCode(String email){
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set("Code:"+email,code, Duration.ofMinutes(5));
        sendVerifyCode(email,code);
    }

    private void sendVerifyCode(String to,String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("2035160857@qq.com");
        message.setSubject("ai_planer验证码");
        message.setText("您的验证码是：" + code + "，5分钟内有效");
        mailSender.send(message);
    }
}
