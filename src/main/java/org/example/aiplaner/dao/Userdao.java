package org.example.aiplaner.dao;

import org.example.aiplaner.Entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface Userdao extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    /**
     * 一次 SQL 把用户和余额都取回来（left join fetch），避免懒加载造成的 N+1。
     * 注意 JPQL 里用的是实体属性名：UserEntity 的字段叫 balance（不是数据库列名 balanceID）。
     */
    @Query("select u from UserEntity u left join fetch u.balance where u.username = :name")
    Optional<UserEntity> findByUsernameWithBalance(@Param("name") String name);

    /** 带余额的用户（供需要遍历用户列表的场景使用）。 */
    @EntityGraph(attributePaths = "balance")
    List<UserEntity> findAllByOrderByIdAsc();

    /** 套接字的属性路径：balance 是关联对象，穿透到它的 id 要写 balance_Id。 */
    boolean existsByBalance_Id(Integer balanceId);

    /** 还没分配余额记录的用户（user.balanceID 为 NULL）。 */
    List<UserEntity> findByBalanceIsNull();

    /** 余额大于指定值的用户。 */
    List<UserEntity> findByBalance_BalanceGreaterThan(BigDecimal min);

    /** 取一个还没分配余额记录的用户，用于补数据。 */
    Optional<UserEntity> findFirstByBalanceIsNull();
}
