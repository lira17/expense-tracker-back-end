package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.UserCreateDto;
import com.lira17.expensetracker.dto.get.UserGetDto;
import com.lira17.expensetracker.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public abstract class UserMapper {
    @Autowired
    public PasswordEncoder bcryptEncoder;

    public abstract UserGetDto convertToDto(User user);

    public abstract List<UserGetDto> convertToDto(Iterable<User> users);

    @Mapping(target = "password", expression = "java(bcryptEncoder.encode(userCreateDto.password()))")
    @Mapping(target = "id", ignore = true)
    public abstract User convertToUser(UserCreateDto userCreateDto);
}
