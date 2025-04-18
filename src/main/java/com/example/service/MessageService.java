package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.*;
import com.example.exception.BadMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import java.util.Optional;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) throws BadMessageException{
        // Check message if message text is blank
        if(message.getMessageText().isBlank()){
            throw new BadMessageException();
        }
        // Check message length
        else if(message.getMessageText().length() > 255){
            throw new BadMessageException();
        }
        // Check if postedby is real
        else if(accountRepository.findAccountByAccountId(message.getPostedBy()) == null){
            throw new BadMessageException();
        }

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId){
        // Find message by id
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        return null;
    }

    public int deleteMessageById(int messageId){
        if(messageRepository.existsById(messageId)){
            Message message = getMessageById(messageId);
            messageRepository.delete(message);
            return 1;
        }
     
        return 0;
    }

    public void updateMessageById(int messageId,Message message) throws Exception{
        // Check update conditions
        if(!messageRepository.existsById(messageId)){
            throw new Exception();
        }
        else if(message.getMessageText().length() > 255 || message.getMessageText().isBlank()){
            throw new Exception();
        }
        else{
            // Get old message and update its text, then save to database
            Message oldMessage = messageRepository.getById(messageId);
            oldMessage.setMessageText(message.getMessageText());
            messageRepository.save(oldMessage);
            return;
        }
    }

    public List<Message> getAccountMessages(int accountId){
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
