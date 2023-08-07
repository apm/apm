package com.example.apm.controller;

import com.example.apm.DataNotFoundException;
import com.example.apm.entity.SiteUser;
import com.example.apm.form.UserCreateForm;
import com.example.apm.repository.UserRepository;
import com.example.apm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Please enter your login information.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doLogin(@RequestParam String username, @RequestParam String password) {
        // 사용자 이름과 비밀번호를 확인하여 로그인 검증
        Optional<SiteUser> userOptional = userRepository.findByusername(username);
        if (userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "로그인 성공!");
                return ResponseEntity.ok(response);
            }
        }

        // 로그인 실패
        Map<String, String> response = new HashMap<>();
        response.put("error", "로그인 실패. 사용자 이름 또는 비밀번호가 올바르지 않습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody UserCreateForm userCreateForm) {
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "패스워드가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 사용자 이름이 이미 데이터베이스에 존재하는지 확인
        Optional<SiteUser> existingUser = userRepository.findByusername(userCreateForm.getUsername());
        if (existingUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "이미 사용 중인 사용자 이름입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1());

        Map<String, String> response = new HashMap<>();
        response.put("message", "사용자가 성공적으로 생성되었습니다.");
        return ResponseEntity.ok(response);
    }
}
