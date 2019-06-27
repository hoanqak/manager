package com.manager.service.UserServiceImpl;

import com.manager.dto.LoginDTO;
import com.manager.md5.MD5;
import com.manager.model.User;
import com.manager.repository.UserRepository;
import com.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MD5 md5;
    public ResponseEntity<LoginDTO> login(LoginDTO loginDTO){
        List<User> listUser  = userRepository.findAll();
        int count = 0;
        for(User user : listUser){
            if(user.getEmail().equals(loginDTO.getEmail()) && user.getPassword().equals(md5.convertToMD5(loginDTO.getPassword()))){
                return new ResponseEntity("LOGIN_SUCCESS", HttpStatus.OK);
            }
            else if(user.getEmail().equals(loginDTO.getEmail())){
                count ++;
            }
        }
        if(count == 0){
            return new ResponseEntity("NO_EXIST_USER", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("WRONG_PASSWORD", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Boolean> forgotPassword(String email) {
        return null;
    }


    @Override
    public ResponseEntity<User> getUserById(int id) {
        return null;
    }

    @Override
    public ResponseEntity<User> editProfile(int id) {
        return null;
    }

}
