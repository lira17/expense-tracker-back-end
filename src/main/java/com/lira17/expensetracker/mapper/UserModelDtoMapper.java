package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.UserCreateDto;
import com.lira17.expensetracker.dto.get.UserGetDto;
import com.lira17.expensetracker.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class UserModelDtoMapper extends BaseBalanceEntityMapper implements ModelDtoMapper<User, UserGetDto, UserCreateDto> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    private static final Type LIST_OF_USERS_TYPE = new TypeToken<List<UserGetDto>>() {
    }.getType();

    @Override
    public UserGetDto mapToDto(User model) {
        return modelMapper.map(model, UserGetDto.class);
    }

    @Override
    public List<UserGetDto> mapToDtoList(List<User> modelList) {
        return modelMapper.map(modelList, LIST_OF_USERS_TYPE);
    }

    @Override
    public User mapToModel(UserCreateDto dto) {
        var user = modelMapper.map(dto, User.class);
        user.setPassword(bcryptEncoder.encode(dto.getPassword()));
        return user;
    }
}
