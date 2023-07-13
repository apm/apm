package com.example.apm.controller;

import com.example.apm.form.UserCreateForm;
import com.example.apm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    } // 회원가입 템플릿 렌더링

    @PostMapping("/signup") //회원가입 진행
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        userService.create(userCreateForm.getUserId(), userCreateForm.getPassword1());

        return "redirect:/";
    }

    @GetMapping("/signup/{userId}")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@PathVariable String userId){
        return ResponseEntity.ok(userService.checkUserIdDuplicate(userId));
    }

}
