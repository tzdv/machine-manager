package com.mycopmany.myproject.machineapi.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserToGet> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserToGet::fromModel)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
