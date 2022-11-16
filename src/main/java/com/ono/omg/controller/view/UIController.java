package com.ono.omg.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/accounts/signup")
    public String register() {
        return "account/accountRegisterForm";
    }

    @GetMapping("/accounts/login")
    public String accountsLoginForm() {
        return "account/accountLoginForm";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "account/adminLoginForm";
    }

}

