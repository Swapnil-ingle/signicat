package com.swapnil.signicat.repository;

import com.swapnil.signicat.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByUsername(String username);
}
