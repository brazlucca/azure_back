package com.project.fiapnotes.controller;

import com.project.fiapnotes.dtos.AuthDto;
import com.project.fiapnotes.dtos.CreateUserDto;
import com.project.fiapnotes.dtos.TokenDto;
import com.project.fiapnotes.dtos.UserDto;
import com.project.fiapnotes.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = {"http://localhost:3000", "https://viniroxo.github.io/fiap-note/", "https://viniroxo.github.io"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public TokenDto login(@RequestBody AuthDto authDto) {
        return userService.login(authDto);
    }

    @PostMapping
    public UserDto create(@RequestBody CreateUserDto createUserDto) {
        return userService.create(createUserDto);
    }
}
