package com.example.test.mapper.user;

import com.example.test.dto.user.UserRegistrationRequestDto;
import com.example.test.dto.user.UserRegistrationResponseDto;
import com.example.test.model.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface UserMapper {
    UserRegistrationResponseDto toUserResponse(User user);

    User toUser(UserRegistrationRequestDto registrationRequestDto);
}
