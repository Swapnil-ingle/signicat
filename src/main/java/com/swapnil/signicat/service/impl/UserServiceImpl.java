package com.swapnil.signicat.service.impl;

import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.exception.UserNotFoundException;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.repository.UserRepository;
import com.swapnil.signicat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<Subject> getByUserId(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Map<String, Object> getClaims(Subject user) {
        Map<String, Object> claimsMap = new HashMap<>();

        claimsMap.put("sub", user.getId());
        claimsMap.put("username", user.getUsername());
        claimsMap.put("groups", user.getUserGroups().stream().map(UserGroupDTO::from).collect(Collectors.toList()));

        return claimsMap;
    }

    @Override
    @Transactional
    public boolean deleteUserByUsername(String username) {
        Subject user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        userRepository.delete(user);
        return true;
    }

    @Override
    public Optional<Subject> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
