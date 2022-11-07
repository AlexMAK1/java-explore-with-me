package ru.practicum.main_service.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.dto.UserShortDto;
import ru.practicum.main_service.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
