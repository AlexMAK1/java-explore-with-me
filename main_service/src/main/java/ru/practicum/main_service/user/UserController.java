package ru.practicum.main_service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "from", defaultValue = "0")
                                     Integer from, @RequestParam(name = "size",
            defaultValue = "10") Integer size, @RequestParam(required = false) List<Long> ids) {
        log.info("Get all requests from={}, size={}", from, size);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        if(ids==null || ids.isEmpty()){
            return userService.getUsers(pageRequest);
        }
        return userService.getUsersWithIds(ids, pageRequest);
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable("id") long id) {
        return userService.update(userDto, id);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
