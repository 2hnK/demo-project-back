package com.hun2.demoproject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        String username = authentication.getName();
        return Map.of("username", username);
    }

    /*
     * CustomUserDetailsService가 현재 권한을 빈 목록으로 넣고
     * 있어 @PreAuthorize("hasAuthority('ADMIN')")는 항상 거절됩니다. 테스트 단계라면 일단 어노테이션을
     * 비활성화하거나, 롤을 부여하는 로직을 추후 추가하세요.
     */
    // @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin-only")
    public String adminOnly() {
        return "ok";
    }

    /*
     * UserController의 /{userId} 경로에서 userId가 username인지 id(Long)인지 결정을 권장합니다. PK로
     * 접근할 계획이면 @PathVariable Long userId로 맞추고 리포지토리는 findById를 사용하세요. 현재는 TODO라 컴파일
     * 이슈는 없습니다.
     */
    // TODO: 유저 목록 조회
    @GetMapping("/")
    public List<User> getUsers() {
        return null;
    }

    // TODO: 유저 정보 조회
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return null;
    }

    // TODO: 유저 정보 수정
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return null;
    }

    // TODO: 회원 탈퇴
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        return;
    }

}
