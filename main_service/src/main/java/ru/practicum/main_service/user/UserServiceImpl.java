package ru.practicum.main_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.exception.ValidationException;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        if (!userDto.getEmail().contains("@")) {
            log.error("Error, validation failed. Email must contain @: {}", userDto.getEmail());
            throw new ValidationException("Error, validation failed. Email must contain @");
        }
        User user = UserConverter.toUser(userDto);
        log.info("Сохраняем нового пользователя: {}", user);
        return UserConverter.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUser(long id) {
        if (userRepository.findById(id).isEmpty()) {
            log.error("Error, validation failed. User with given id does not exist: {}", id);
            throw new NotFoundException("Error, validation failed. User with given id does not exist");
        } else {
            log.info("Find user with id: {} {}", id, userRepository.getReferenceById(id));
            return UserConverter.toUserDto(userRepository.getReferenceById(id));
        }
    }

    @Override
    public List<UserDto> getUsersWithIds(List<Long> ids, PageRequest pageRequest) {
        List<User> users = userRepository.findAllUsers(ids, pageRequest);
        log.info("Finding all existing users: {}", users);
        return users.stream()
                .map(UserConverter::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsers(PageRequest pageRequest) {
        Page<User> users = userRepository.findAll(pageRequest);
        log.info("Finding all existing users: {}", users);
        return users.stream()
                .map(UserConverter::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = userRepository.getReferenceById(id);
        String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null) {
            user.setEmail(email);
        }
        log.info("Updating user data: {}", user);
        return UserConverter.toUserDto(userRepository.save(user));
    }

    @Override
    public void delete(long id) {
        log.info("Delete user with id: {}", id);
        userRepository.deleteById(id);
    }
}
