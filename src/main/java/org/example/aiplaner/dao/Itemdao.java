package org.example.aiplaner.dao;

import org.example.aiplaner.Entity.Todoitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Itemdao extends JpaRepository<Todoitem,Integer> {
     List<Todoitem> findByUserID(Integer userID);

}
