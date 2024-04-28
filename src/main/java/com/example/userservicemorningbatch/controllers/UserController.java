package com.example.userservicemorningbatch.controllers;

import com.example.userservicemorningbatch.dtos.*;
import com.example.userservicemorningbatch.exceptions.InvalidPasswordException;
import com.example.userservicemorningbatch.exceptions.InvalidTokenException;
import com.example.userservicemorningbatch.models.Token;
import com.example.userservicemorningbatch.models.User;
import com.example.userservicemorningbatch.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup") // localhost:8080/users/signup
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getName()
        );

        //get UserDto from User;
        return UserDto.from(user);
    }

    @PostMapping("/login") // localhost:8080/users/login
    public LogInResponseDto login(@RequestBody LogInRequestDto requestDto) throws InvalidPasswordException {
        Token token = userService.login(requestDto.getEmail(),
                requestDto.getPassword());

        LogInResponseDto responseDto = new LogInResponseDto();
        responseDto.setToken(token);

        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto requestDto) throws InvalidTokenException {
        ResponseEntity<Void> responseEntity = null;
        try {
            userService.logout(requestDto.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Something went wrong");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}