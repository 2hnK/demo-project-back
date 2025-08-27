package com.hun2.demoproject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hun2.demoproject.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {

    // TODO: 유저 목록 조회
    @GetMapping("/")
    public List<User> getUsers() {
        return null;
    }

    // TODO: 유저 정보 조회
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return null;
    }

    // TODO: 유저 정보 수정
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User user) {
        return null;
    }

    // TODO: 회원 탈퇴
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        return;
    }

}
