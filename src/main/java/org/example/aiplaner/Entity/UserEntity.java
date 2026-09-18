package org.example.aiplaner.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "user", schema = "ai-planer")
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


    /**
     * 一个用户一条余额记录（一对一）。
     *
     * <p>外键在本表：user.balanceID -> apibalance.id，所以 UserEntity 是关联的"拥有端"，
     * 由它负责写这个外键列。是否级联删除取决于业务：这里余额属于用户，用户删了余额也没意义，
     * 所以 cascade = ALL + orphanRemoval = true。</p>
     *
     * <p>LAZY 是默认值，这里显式写出来：查用户时不会顺带把余额查出来，
     * 需要余额时用 {@code Userdao#findByUsernameWithBalance} 的 join fetch 一次取回，避免 N+1。</p>
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "balanceID", referencedColumnName = "id")
    private Apibalance balance;

    public Apibalance getBalance() {
        return balance;
    }

    public void setBalance(Apibalance balance) {
        this.balance = balance;
    }

    // ---- 兼容旧调用方的别名，新代码请用 getBalance / setBalance ----

    public Apibalance getBalanceID() {
        return balance;
    }

    public void setBalanceID(Apibalance balance) {
        this.balance = balance;
    }


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
