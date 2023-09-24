package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Создать пользователя " + userDto);
        UserDto userDtoResult = userService.addUser(userDto);
        return new ResponseEntity<>(userDtoResult, HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> getUsersById(@RequestParam(defaultValue = "") Set<Long> ids,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получить пользователей по id " + ids);
        return userService.findUsersById(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удалить пользователя по id " + userId);
        userService.deleteUserById(userId);
    }
}
