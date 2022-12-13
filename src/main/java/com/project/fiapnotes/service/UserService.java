package com.project.fiapnotes.service;

import com.project.fiapnotes.dtos.AuthDto;
import com.project.fiapnotes.dtos.CreateUserDto;
import com.project.fiapnotes.dtos.TokenDto;
import com.project.fiapnotes.dtos.UserDto;

public interface UserService {
    TokenDto login(AuthDto authDto);

    UserDto create(CreateUserDto createUserDto);
}
