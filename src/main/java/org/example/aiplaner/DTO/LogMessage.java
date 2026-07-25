package org.example.aiplaner.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

public class LogMessage {
    private String username;
    private String password;
    @JsonProperty("Email")
    private String Email;
    private String name;
    private String code;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {return Email;}
    public void setEmail(String email) {Email = email;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
