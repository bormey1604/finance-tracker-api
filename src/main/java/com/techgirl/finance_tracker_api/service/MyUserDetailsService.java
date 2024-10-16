package com.techgirl.finance_tracker_api.service;

import com.techgirl.finance_tracker_api.model.MyUser;
import com.techgirl.finance_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> findUser = userRepository.findByUsername(username);

        if(findUser.isEmpty()) {
            findUser = userRepository.findByEmail(username);
        }

        if(findUser.isPresent()){
            var user = findUser.get();

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        }

        throw new UsernameNotFoundException(username);

    }
}
