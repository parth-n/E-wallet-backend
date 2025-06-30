package com.example.Controller;


import com.example.DTO.UserDTO;
import com.example.Service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user-service")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService ;

    @PostMapping("/user")
    public Long createUser(@RequestBody @Valid UserDTO userDTO) throws ExecutionException, InterruptedException {
        LOGGER.info("Processing User creation request") ;
        return userService.createUser(userDTO);
    }
}
