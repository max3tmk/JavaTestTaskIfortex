package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.UserResponseDTO;
import com.example.java_ifortex_test_task.entity.User;
import com.example.java_ifortex_test_task.mapper.UserMapper;
import com.example.java_ifortex_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO getUserWithMostSessions() {
        try {
            log.info("Fetching user with most sessions");
            User user = userRepository.findUserWithMostSessions();
            if (user == null) {
                log.warn("No user found with sessions");
                return null;
            }
            return userMapper.toDto(user);
        } catch (Exception e) {
            log.error("Failed to fetch user with most sessions", e);
            return null;
        }
    }

    public List<UserResponseDTO> getUsersWithAtLeastOneMobileSession() {
        try {
            log.info("Fetching users with at least one mobile session");
            List<User> users = userRepository.findUsersWithMobileSession();
            return users.stream()
                    .map(userMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch users with mobile sessions", e);
            return Collections.emptyList();
        }
    }
}