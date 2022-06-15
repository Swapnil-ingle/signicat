package com.swapnil.signicat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user_group")
@Getter
@Setter
public class UserGroup extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany()
    @JoinTable(name = "subject_user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonIgnoreProperties
    private Set<Subject> users = new HashSet<>();
}
