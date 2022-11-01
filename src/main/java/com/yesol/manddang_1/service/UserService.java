package com.yesol.manddang_1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesol.manddang_1.repository.UserRepository;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public String save(final User user, HttpSession sess) {
        Date now = new Date();
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setUserPwd(passwordEncoder.encode(user.getPassword()));
            user.setUserAuth("USER");
            user.setRegDate(now);
            User getUser = userRepository.save(user);
            result = mapper.writeValueAsString(getUser);

        } catch(Exception e){
            logger.error("UserService.save ERROR :{}",e);
        }
        return result;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("###############service 진입");
        String userId = username;
        User getUser = new User();
        try{
            getUser = (userRepository.findByUserId(userId)).get();
        }catch(NoSuchElementException e){ //아이디가 잘못됐을 경우
            throw new UsernameNotFoundException("User not authorized.");
        }catch(Exception e){
            logger.error("UserService.loadUserByUsername ERROR :{}",e);
        }
        return getUser;
    }
}
