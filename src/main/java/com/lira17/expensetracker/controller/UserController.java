package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.dto.create.UserCreateDto;
import com.lira17.expensetracker.dto.get.UserGetDto;
import com.lira17.expensetracker.mapper.UserMapper;
import com.lira17.expensetracker.security.JwtRequest;
import com.lira17.expensetracker.security.JwtResponse;
import com.lira17.expensetracker.security.JwtTokenUtil;
import com.lira17.expensetracker.security.JwtUserDetailsService;
import com.lira17.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@CrossOrigin
@Tag(name = "User Management", description = "User management API")
@RequestMapping(API + "users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserGetDto> getAllUsers() {
        return userMapper.convertToDto(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "User is not found", content = @Content)})
    public UserGetDto getUserById(@PathVariable("id") Long id) {
        return userMapper.convertToDto(userService.getUserById(id));
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user")
    public UserGetDto createUser(@RequestBody UserCreateDto userCreateDto) {
        var user = userMapper.convertToUser(userCreateDto);
        return userMapper.convertToDto(userService.addUser(user));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found", content = @Content)})
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PostMapping(value = "/authenticate")
    @Operation(summary = "Get jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content),
            @ApiResponse(responseCode = "404", description = "User is not found", content = @Content)})
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.username(), jwtRequest.password()));

        var userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequest.username());

        return new JwtResponse(jwtTokenUtil.generateToken(userDetails));
    }
}
