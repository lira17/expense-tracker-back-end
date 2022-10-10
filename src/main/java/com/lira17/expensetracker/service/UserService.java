package com.lira17.expensetracker.service;

import com.lira17.expensetracker.exception.NotFoundException;
import com.lira17.expensetracker.model.User;
import com.lira17.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class));
    }

    @Transactional
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new NotFoundException(User.class));
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
