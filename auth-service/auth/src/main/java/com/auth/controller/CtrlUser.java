package com.auth.controller;

import com.auth.dto.in.UserRequest;
import com.auth.dto.out.UserResponse;
import com.auth.service.SvcUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class CtrlUser {

    @Autowired
    private SvcUser svcUser;

    @PostMapping
    public String create(@Valid @RequestBody UserRequest request) {
        return svcUser.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return svcUser.getUsers();
    }

}