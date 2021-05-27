//package com.epam.esm.service.security;
//
//import com.epam.esm.dao.UserRepository;
//import com.epam.esm.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(userName)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userName));
//        return UserDetailsImpl.build(user);
//    }
//}