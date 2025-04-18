package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;

import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
 @Component
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody Account body){
        try{
            Account newAccount = accountService.register(body);
            return ResponseEntity.status(200).body(newAccount);
        } catch(DuplicateUsernameException e){
            return ResponseEntity.status(409).body("Duplicate Username");
        } catch(BadUsernameException e){
            return ResponseEntity.status(400).body("Password too short");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody Account body){
        try{
            Account login = accountService.login(body);
            return ResponseEntity.status(200).body(login);
        } catch(UnauthorizedLoginException e){
            return ResponseEntity.status(401).body("Anauthorized Login");
        }
    }

    @PostMapping(value = "/messages")
    public ResponseEntity createMessages(@RequestBody Message body){
        try{
            Message newMessage = messageService.createMessage(body);
            return ResponseEntity.status(200).body(newMessage);
        } catch(BadMessageException e){
            return ResponseEntity.status(400).body("Invalid Message");
        }
    }

    @GetMapping(value = "/messages")
    public ResponseEntity getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping(value = "/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId){
    
        Message message = messageService.getMessageById(messageId);

        return ResponseEntity.status(200).body(message);
    }

    @DeleteMapping(value = "/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable int messageId){
        
        int rowsDel = messageService.deleteMessageById(messageId);
        
        if(rowsDel == 1){
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).body("");
    }

    @PatchMapping(value = "/messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable int messageId, @RequestBody Message message){
        try{
            messageService.updateMessageById(messageId,message);
            return ResponseEntity.status(200).body(1);
        } catch(Exception e){
            return ResponseEntity.status(400).body("Client Error");
        }
    }

    @GetMapping(value = "/accounts/{accountId}/messages")
    public ResponseEntity getMessagesFromAccount(@PathVariable int accountId){
        List<Message> messages = messageService.getAccountMessages(accountId);
        return ResponseEntity.status(200).body(messages);
    }
    
}
