package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto user) {
        if (user.getName() == null || user.getEmail() == null || user.getEmail().isBlank() || user.getName().isBlank()) {
            throw new RequestException("Поля name и email не могут быть пустыми");
        }
        if (user.getName().length() < 2 || user.getName().length() > 250) {
            throw new RequestException("Количество символов имени меньше 2 или больше 250");
        }
        if (user.getEmail().length() < 6 || user.getEmail().length() > 254) {
            throw new RequestException("Количество символов email меньше 6 или больше 254");
        }
        String[] emailParts = user.getEmail().split("@");
        if (emailParts[0].length() > 64) {
            throw new RequestException("Количество символов email больше 64");
        }
        String[] domainParts = emailParts[1].split("\\.");
        Arrays.stream(domainParts).forEach(userPart -> {
                        if (userPart.length() > 63) {
                            throw new RequestException("Количество символов email больше 63");
                        }
                    }
                );
        return UserMapper.userToUserDto(userRepository.save(UserMapper.userDtoToUser(user)));
    }

    @Override
    public List<UserDto> findUsersById(Set<Long> usersId, int from, int size) {
        if (usersId.isEmpty()) {
            return UserMapper.usersToUsersDto(userRepository.findAll(PageRequest.of(from, size)).getContent());
        }
        return UserMapper.usersToUsersDto(userRepository.getUsersByIdIsIn(usersId, PageRequest.of(from, size))
                .getContent());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
