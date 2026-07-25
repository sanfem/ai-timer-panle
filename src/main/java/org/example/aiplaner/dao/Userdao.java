package org.example.aiplaner.dao;

import org.example.aiplaner.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Userdao extends JpaRepository<UserEntity, Long> {
     Optional<UserEntity> findByUsername(String username);
}
