package com.example.repository;

import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    // Checks if a username is already in the database
    //@Query("FROM Account WHERE username = :username")
    //List<Account> CheckUsername(@Param("username") String username);
    Account findAccountByUsername(String username);

    // Finds Account based on account id;
    Account findAccountByAccountId(int AccountId);

}
