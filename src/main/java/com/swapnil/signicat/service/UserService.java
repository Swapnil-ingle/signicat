package com.swapnil.signicat.service;

import com.swapnil.signicat.model.Subject;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<Subject> getByUserId(Long id);

    Map<String, Object> getClaims(Subject user);

    Optional<Subject> getByUsername(String username);

    boolean deleteUserByUsername(String username);
}
