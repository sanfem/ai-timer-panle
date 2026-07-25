package org.example.aiplaner.Entity;


import jakarta.persistence.*;
import tools.jackson.databind.ser.jdk.JDKKeySerializers;

@Entity
@Table(name="user")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true) // 映射字段
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="name",columnDefinition = "VARCHAR(255) DEFAULT 'momo'")
    private String name="momo";
    @Column(name="Email")
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
