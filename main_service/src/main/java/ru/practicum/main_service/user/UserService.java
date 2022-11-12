package ru.practicum.main_service.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto getUser(long id);

    List<UserDto> getUsersWithIds(List<Long> ids, PageRequest pageRequest);

    List<UserDto> getUsers(PageRequest pageRequest);

    UserDto update(UserDto userDto, long id);

    void delete(long id);
}
