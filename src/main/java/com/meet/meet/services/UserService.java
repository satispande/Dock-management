package com.meet.meet.services;

import com.meet.meet.dtos.RegistrationDto;
import com.meet.meet.dtos.UserDto;
import com.meet.meet.models.UserEntity;

public interface UserService {
    UserDto registerUser(RegistrationDto registerDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

}
