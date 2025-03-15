package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }

    public Account register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) {
        Optional<Account> existingAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        return existingAccount.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

}
