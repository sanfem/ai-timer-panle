package org.example.aiplaner.controler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aiplaner.DTO.LogMessage;
import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.Service.LoginService;
import org.example.aiplaner.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();
    }

    // ==================== 登录测试 ====================

    @Test
    void testLogin_Success() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("123456");

        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("admin");
        logMessage.setPassword("123456");

        when(loginService.login("admin", "123456")).thenReturn(user);
        when(jwtUtils.generateToken(user)).thenReturn("mockToken123");  // ← Mock generateToken

        mockMvc.perform(post("/Login/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.token").value("mockToken123"));
    }

    @Test
    void testLogin_Failure_WrongPassword() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("testUser");
        logMessage.setPassword("wrongPassword");

        when(loginService.login("testUser", "wrongPassword")).thenReturn(null);

        mockMvc.perform(post("/Login/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("密码或账号错误"));
    }

    @Test
    void testLogin_Failure_EmptyUsername() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("");
        logMessage.setPassword("123456");

        mockMvc.perform(post("/Login/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码已经存在"));
    }

    @Test
    void testLogin_Failure_EmptyPassword() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("testUser");
        logMessage.setPassword("");

        mockMvc.perform(post("/Login/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码已经存在"));
    }

    @Test
    void testLogin_Failure_EmptyBoth() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("");
        logMessage.setPassword("");

        mockMvc.perform(post("/Login/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码已经存在"));
    }

    // ==================== 注册测试 ====================

    @Test
    void testRegister_Success() throws Exception {
        UserEntity newUser = new UserEntity();
        newUser.setId(2L);
        newUser.setUsername("newUser");
        newUser.setPassword("newPassword");

        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("newUser");
        logMessage.setPassword("newPassword");

        // Controller 里 user.isPresent() 才返回 200，所以 Register 必须有值
        when(loginService.Register(any(UserEntity.class))).thenReturn(newUser);

        mockMvc.perform(post("/Login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testRegister_Failure_UserExists() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("testUser");
        logMessage.setPassword("123456");

        // Controller 里 else 分支返回 401，所以 Register 返回 null
        when(loginService.Register(any(UserEntity.class))).thenReturn(null);

        mockMvc.perform(post("/Login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码已经存在"));
    }

    @Test
    void testRegister_Failure_EmptyUsername() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("");
        logMessage.setPassword("123456");

        mockMvc.perform(post("/Login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码不能为空"));
    }

    @Test
    void testRegister_Failure_EmptyPassword() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("testUser");
        logMessage.setPassword("");

        mockMvc.perform(post("/Login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码不能为空"));
    }

    @Test
    void testRegister_Failure_EmptyBoth() throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setUsername("");
        logMessage.setPassword("");

        mockMvc.perform(post("/Login/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logMessage)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("账号或密码不能为空"));
    }
}