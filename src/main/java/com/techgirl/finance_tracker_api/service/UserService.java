package com.techgirl.finance_tracker_api.service;

import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(MyUser myUser){
        myUser.setPassword(encoder.encode(myUser.getPassword()));
        userRepository.save(myUser);
    }


    public MyUser findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
