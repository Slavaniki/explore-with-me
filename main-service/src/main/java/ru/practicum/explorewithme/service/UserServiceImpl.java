package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
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
