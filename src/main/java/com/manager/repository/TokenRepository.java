package com.manager.repository;

import com.manager.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("select t from Token as t where t.id =:id")
    public Token getTokenById(@Param("id") int id);

    @Query("select t from Token as t where t.token=:code")
    public Token getTokenByCode(@Param("code") String code);
}
