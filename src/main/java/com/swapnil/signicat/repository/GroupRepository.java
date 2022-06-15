package com.swapnil.signicat.repository;

import com.swapnil.signicat.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByName(String name);
}
