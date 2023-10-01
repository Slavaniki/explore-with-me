package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDto addUser(UserDto user);

    List<UserDto> findUsersById(Set<Long> usersId, int from, int size);

    void deleteUserById(Long id);
}
