package com.project.fiapnotes.service;

import com.project.fiapnotes.config.security.JwtUtil;
import com.project.fiapnotes.dtos.AuthDto;
import com.project.fiapnotes.dtos.CreateUserDto;
import com.project.fiapnotes.dtos.TokenDto;
import com.project.fiapnotes.dtos.UserDto;
import com.project.fiapnotes.models.UserModel;
import com.project.fiapnotes.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Override
    public TokenDto login(AuthDto authDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authDto.getUsername(),
                    authDto.getPassword()
            ));
        } catch (DisabledException | BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid.credentials");
        }
        String token = jwtUtil.generateToken(authDto.getUsername());
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        return tokenDto;
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        UserModel userModel = new UserModel();
        userModel.setUsername(createUserDto.getUsername());
        userModel.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        UserModel savedUser = userRepository.save(userModel);

        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setUsername(savedUser.getUsername());
        return userDto;
    }
}
