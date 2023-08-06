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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login_form";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody UserCreateForm userCreateForm) {
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"패스워드가 일치하지 않습니다.\"}");
        }

        // 사용자 이름이 이미 데이터베이스에 존재하는지 확인
        Optional<SiteUser> existingUser = userRepository.findByusername(userCreateForm.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"이미 사용 중인 사용자 이름입니다.\"}");
        }

        userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1());

        return ResponseEntity.ok("{\"message\": \"사용자가 성공적으로 생성되었습니다.\"}");
    }

}
