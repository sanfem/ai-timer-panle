package org.example.aiplaner;

import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.dao.Userdao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiPlanerApplicationTests {

    private DataSource dataSource;
    private Userdao userdao;


    @Autowired
    public AiPlanerApplicationTests(DataSource dataSource , Userdao userdao) {
        this.dataSource = dataSource;
        this.userdao = userdao;
    }
    @Test
    void contextLoads() {
    }

    @Test
    void dateBaseTest() throws SQLException {
        try(Connection coon=dataSource.getConnection()){
            assertNotNull(coon);
            System.out.println("连接成功");
        }
    }

    @Test
    void connectionTest(){
        Optional<UserEntity> ans=userdao.findByUsername("admin");
        assertNotNull(ans,"未找到用户 admin");
        System.out.println("通过"+ans.get().getUsername());
    }
}
