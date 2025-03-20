package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;

import java.util.List;
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }        
        // Check if user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does't exist");
        }
    
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Integer deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1; //1 row deleted
        }
        return null;
    }

    public Integer updateMessage(Integer id, Message message) {
        Message existingMessage = messageRepository.findById(id).orElse(null);
        if (existingMessage == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found");
        }
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message");
        }

        existingMessage.setMessageText(message.getMessageText());
        messageRepository.save(existingMessage);
        return 1;
    }

    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}
