package com.meet.meet.mappers;

import com.meet.meet.dtos.UserDto;
import com.meet.meet.models.UserEntity;

public class UserMapper {
	public static UserEntity mapToUser(UserDto userDto) {
		
		UserEntity ur = new UserEntity();
		ur.setEmail(userDto.getEmail());
		ur.setFullName(userDto.getFullName());
		ur.setId(userDto.getId());
		ur.setUsername(userDto.getUsername());
		return ur;

	}

	public static UserDto mapToUserDto(UserEntity user) {
		
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		return userDto;

	}
}
