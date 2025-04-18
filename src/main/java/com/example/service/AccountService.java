package com.example.service;

import com.example.exception.*;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) throws DuplicateUsernameException, BadUsernameException{
        // Check username Conditions
        if(account.getUsername().isBlank()){
            throw new BadUsernameException();
        }
        else if(account.getPassword().length() < 4){
            throw new BadUsernameException();
        }
        // Check Dupe Username
        else if(accountRepository.findAccountByUsername(account.getUsername()) != null){
            throw new DuplicateUsernameException();
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) throws UnauthorizedLoginException{
        // Get account by username
        Account loginAccount = accountRepository.findAccountByUsername(account.getUsername());
        
        // See if account was found
        if(loginAccount == null){
            throw new UnauthorizedLoginException();
        }
        // Check is passwords match
        else if(loginAccount.getPassword().equals(account.getPassword())){
            return loginAccount;
        }
        else{
            throw new UnauthorizedLoginException();
        }
    }
}
